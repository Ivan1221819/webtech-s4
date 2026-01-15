package webtech_semester_4.webtech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import webtech_semester_4.webtech.auth.AppUser;
import webtech_semester_4.webtech.auth.AppUserRepository;
import webtech_semester_4.webtech.auth.AuthService_Mock;
import webtech_semester_4.webtech.mealdb.MealsController;
import webtech_semester_4.webtech.mealdb.TheMealDbClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class Test_Backend {

    @Mock TheMealDbClient mealDbClient;
    @Mock AppUserRepository appUserRepository;
    @Mock PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new MealsController(mealDbClient)).build();
    }


    // MealsController Tests


    // 1)
    @Test
    @DisplayName("Testet, ob der MealsController einen Mock Request korrekt annimmt und die richtigen Daten liefert")
    void meals_search_returns200_andMealsArray() throws Exception {
        doReturn(mealsBody("123", "Cake")).when(mealDbClient).searchByName("cake");

        mvc.perform(get("/api/meals").param("q", "cake"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.meals[0].idMeal").value("123"))
                .andExpect(jsonPath("$.meals[0].strMeal").value("Cake"));

        verify(mealDbClient).searchByName("cake");
    }

    // 2)
    @Test
    @DisplayName("Testet, ob irrelevante Leerzeichen entfernt werden")
    void meals_search_trimsQuery() throws Exception {
        doReturn(emptyMealsBody()).when(mealDbClient).searchByName("chicken");

        mvc.perform(get("/api/meals").param("q", "   chicken   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meals").isArray());

        verify(mealDbClient).searchByName("chicken");
    }

    // 3)
    @Test
    @DisplayName("Testet, ob ein Client Aufruf mit leerem String den Status OK zurückgibt")
    void meals_search_withoutQuery_usesEmptyString() throws Exception {
        doReturn(emptyMealsBody()).when(mealDbClient).searchByName("");

        mvc.perform(get("/api/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meals").isArray());

        verify(mealDbClient).searchByName("");
    }

    // 4)
    @Test
    @DisplayName("Testet, ob bei einem Client Fehler der FehlerCode 502 und Error zurückgegeben wird")
    void meals_search_whenClientThrows_returns502_withError() throws Exception {
        when(mealDbClient.searchByName(anyString())).thenThrow(new RuntimeException("boom"));

        mvc.perform(get("/api/meals").param("q", "cake"))
                .andExpect(status().isBadGateway())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.meals").value(nullValue()))
                .andExpect(jsonPath("$.error").value("RuntimeException"));
    }

    // ==========================================================
    // AuthService Tests (kein DB Zugriff, alles Mock)
    // ==========================================================

    // 5)
    @Test
    @DisplayName("Testet, ob beim registrieren das Passwort in ein Hash Code umgewandelt wird")
    void register_hashesPassword_andSavesHashNotPlaintext() {
        when(passwordEncoder.encode("secret123")).thenReturn("HASHED");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(inv -> inv.getArgument(0));

        var service = new AuthService_Mock(appUserRepository, passwordEncoder);
        service.register("a@b.de", "secret123");

        verify(passwordEncoder).encode("secret123");

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());

        AppUser saved = captor.getValue();
        assertEquals("a@b.de", saved.getEmail());
        assertEquals("HASHED", saved.getPasswordHash());
        assertNotEquals("secret123", saved.getPasswordHash());
    }

    // ==========================================================
    // Helpers
    // ==========================================================

    private static Map<String, Object> mealsBody(String id, String name) {
        Map<String, Object> meal = new HashMap<>();
        meal.put("idMeal", id);
        meal.put("strMeal", name);

        Map<String, Object> body = new HashMap<>();
        body.put("meals", List.of(meal));
        return body;
    }

    private static Map<String, Object> emptyMealsBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("meals", List.of());
        return body;
    }
}

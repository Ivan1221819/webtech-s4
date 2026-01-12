package webtech_semester_4.webtech;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import webtech_semester_4.webtech.mealdb.MealsController;
import webtech_semester_4.webtech.mealdb.TheMealDbClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealsController.class)
class Test_Backend {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TheMealDbClient client; // Dummy - Client



    /**
     * Der Testfall, testet
     */
    @Test()
    void search_returnsMeals200() throws Exception {
        Mockito.when(client.searchByName(anyString()))  // Code reagiert bei dem Aufruf der Methode searchByName
                .thenReturn((Map) Map.of("meals", List.of(Map.of("idMeal", "1", "strMeal", "Test Cake"))));     // Test Daten

        // Simulation eines HTTP-Aufrufs auf den Controller
        mvc.perform(get("/api/meals").param("q", "cake"))
                .andExpect(status().isOk())     // Tritt ein, sobald die nötigen Test Daten überliefert wurden  
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.meals[0].idMeal").value("1"))
                .andExpect(jsonPath("$.meals[0].strMeal").value("Test Cake"))
                .andDo(result -> {
            System.out.println("Test erfolgreich ausgeführt!");
            System.out.println("Antwort: " + result.getResponse().getContentAsString());
            }
        );
    }

    @Test
    void search_mealsNullStill200() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("meals", null);

            given(client.searchByName(Mockito.anyString()))
                .willReturn((Map) mockResponse);

        mvc.perform(get("/api/meals").param("q", "unknown"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("✅ Response: " + result.getResponse().getContentAsString()));
    }


    @Test
    void search_clientThrows_resultsIn502() throws Exception {
        Mockito.when(client.searchByName(anyString()))
                .thenThrow(new RuntimeException("boom"));

        mvc.perform(get("/api/meals").param("q", "cake"))
                .andExpect(status().isBadGateway())          // 502
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.meals").doesNotExist())
                .andExpect(jsonPath("$.error").value("RuntimeException"));
    }
}

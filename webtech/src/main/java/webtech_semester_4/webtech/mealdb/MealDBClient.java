package webtech_semester_4.webtech.mealdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import webtech_semester_4.webtech.mealdb.dto.MealDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MealDBClient {

    private final RestTemplate rest = new RestTemplate();

    @Value("${apininja.base-url}") private String baseUrl; // https://api.api-ninjas.com/v2
    @Value("${apininja.api-key}")  private String apiKey;

    // Suche nach Titel
    public List<MealDto> searchByName(String query) {
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/recipe")
                .queryParam("title", query == null ? "" : query)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("[MealDBClient] GET " + url);
        ResponseEntity<MealDto[]> resp = rest.exchange(url, HttpMethod.GET, entity, MealDto[].class);
        MealDto[] body = resp.getBody();
        return body == null ? List.of() : Arrays.asList(body);
    }

    public List<MealDto> lookupMealById(String id) {
        return List.of();
    }
}

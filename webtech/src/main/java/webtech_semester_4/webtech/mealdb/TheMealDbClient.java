package webtech_semester_4.webtech.mealdb;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
public class TheMealDbClient {

    private final RestTemplate http = new RestTemplate();
    private static final String BASE = "https://www.themealdb.com/api/json/v1/1";

    public Map<?, ?> searchByName(String q) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE + "/search.php")
                .queryParam("s", q == null ? "" : q)
                .toUriString();
        return get(url);
    }

    private Map<?, ?> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<Map> resp = http.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        return resp.getBody();
    }
}

package webtech_semester_4.webtech.mealdb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "https://webtech-s4.onrender.com"})
public class MealsController {

    private final TheMealDbClient client;

    public MealsController(TheMealDbClient client) {
        this.client = client;
    }

    // GET /api/meals?q=cake  -> { meals: [...] }  (nie null zurückgeben)
    @GetMapping(value = "/meals", produces = "application/json")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(name = "q", required = false) String q) {
        try {
            Map<?, ?> body = client.searchByName(q == null ? "" : q.trim()); // TheMealDB: { meals: [...] | null }
            Map<String, Object> out = new HashMap<>();
            out.put("meals", body == null ? null : body.get("meals"));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            Map<String, Object> out = new HashMap<>();
            out.put("meals", null);
            out.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(502).body(out);
        }
    }

    // GET /api/meals/{id} -> { meals: [...] } (genau 1)
    @GetMapping(value = "/meals/{id}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> byId(@PathVariable String id) {
        try {
            Map<?, ?> body = client.lookupById(id);
            Map<String, Object> out = new HashMap<>();
            out.put("meals", body == null ? null : body.get("meals"));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            Map<String, Object> out = new HashMap<>();
            out.put("meals", null);
            out.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(502).body(out);
        }
    }

    // test
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("ok", true);
    }
}

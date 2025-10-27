package webtech_semester_4.webtech.mealdb;

import org.springframework.web.bind.annotation.*;
import webtech_semester_4.webtech.mealdb.dto.MealDto;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = {"http://localhost:5173", "https://webtech-s4.onrender.com"}) // für Vite
public class MealController {

    private final MealDBClient client;

    public MealController(MealDBClient client) { this.client = client; }

    @GetMapping(produces = "application/json")
    public List<MealDto> search(@RequestParam(name = "q", required = false) String q) {
        return client.searchByName(q == null ? "" : q);
    }


}

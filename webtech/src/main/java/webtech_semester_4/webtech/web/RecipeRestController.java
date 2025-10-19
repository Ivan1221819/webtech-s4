package webtech_semester_4.webtech.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import webtech_semester_4.webtech.persistence.Recipe;

import java.util.List;

@RestController
public class RecipeRestController {

    private List<Recipe> recipes;

    @GetMapping(path = "/api/v1/recipe")
    public List<Recipe> fetchRecipe() {
        return recipes;
    }

}

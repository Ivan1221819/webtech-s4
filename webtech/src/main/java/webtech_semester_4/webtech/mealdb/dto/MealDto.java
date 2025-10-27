package webtech_semester_4.webtech.mealdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MealDto {

    private String title;
    private String ingredients;
    private String servings;
    private String instructions;


    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getServings() { return servings; }
    public void setServings(String servings) { this.servings = servings; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}

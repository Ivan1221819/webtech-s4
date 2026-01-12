package webtech_semester_4.webtech.mealdbMerkliste;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MERKLISTE_ITEMS")
public class MerklisteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mealId;
    private String title;
    private String thumbnail;

    @Lob
    private String instructions;

    @Lob
    private String ingredients;

    private LocalDateTime createdAt = LocalDateTime.now();

    public MerklisteItem() {}

    public MerklisteItem(String mealId, String title, String thumbnail, String instructions, String ingredients) {
        this.mealId = mealId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }


    public String getMealId() {
        return mealId;
    }
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

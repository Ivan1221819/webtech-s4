package webtech_semester_4.webtech.web.api;

import jakarta.persistence.Column;

public class Recipe {

    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_URL")
    private String image_Url;


    public Recipe(String title, String description, String image_Url) {
        this.title = title;
        this.description = description;
        this.image_Url = image_Url;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_Url() {
        return image_Url;
    }
    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }
}

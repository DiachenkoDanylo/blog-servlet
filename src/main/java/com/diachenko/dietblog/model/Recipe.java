package com.diachenko.dietblog.model;
/*  diet-blog
    16.02.2025
    @author DiachenkoDanylo
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Recipe {

    private int id;
    private String title;
    private String description;
    private int calories;
    private AppUser owner;
    private LocalDateTime createdAt;
    private String titleImage = "";

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Recipe recipe = (Recipe) object;
        return id == recipe.id && calories == recipe.calories && Objects.equals(title, recipe.title) && Objects.equals(description, recipe.description) && Objects.equals(owner, recipe.owner) && Objects.equals(createdAt, recipe.createdAt) && Objects.equals(titleImage, recipe.titleImage);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + calories;
        result = 31 * result + Objects.hashCode(owner);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(titleImage);
        return result;
    }
}

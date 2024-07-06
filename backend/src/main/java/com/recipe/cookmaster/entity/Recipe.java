package com.recipe.cookmaster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RecipeId")
    private long id;

    // Metadata
    private String keywords;
    private long userId;

    // Recipe
    private String title, imageUrl;

    @Lob
    @Column(columnDefinition = "BLOB")
    private String recipeText;

    // Comments
    private List<List<String>> comments;
}



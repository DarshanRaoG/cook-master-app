package com.recipe.cookmaster.service;

import com.recipe.cookmaster.entity.Recipe;

import java.util.List;

public interface RecipeService {
    Recipe addRecipe(Recipe recipe);

    String deleteRecipe(long recipeId);

    String updateRecipe(Recipe recipe);

    List<Recipe> getRecipes();

    Recipe getRecipe(long id);

}

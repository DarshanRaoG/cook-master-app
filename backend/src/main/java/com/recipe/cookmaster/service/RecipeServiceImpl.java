package com.recipe.cookmaster.service;

import com.recipe.cookmaster.entity.Recipe;
import com.recipe.cookmaster.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public String deleteRecipe(long recipeId) {
        recipeRepository.deleteById(recipeId);
        return "Recipe deleted successfully!";
    }

    @Override
    public String updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
        return "Recipe updated successfully!";
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipe(long id) {
        try {
            return recipeRepository.findById(id).get();
        } catch (Exception e) {
            return null;
        }
    }
}

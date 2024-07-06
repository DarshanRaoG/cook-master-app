package com.recipe.cookmaster.controller;

import com.recipe.cookmaster.entity.Recipe;
import com.recipe.cookmaster.entity.User;
import com.recipe.cookmaster.service.RecipeService;
import com.recipe.cookmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cookmaster/recipes")
@CrossOrigin(origins = "http://localhost:5173")
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;

    @GetMapping("/home")
    ResponseEntity<List<Recipe>> getRecipes() {
        List<Recipe> recipes = recipeService.getRecipes();

        if(recipes == null)
            return new ResponseEntity<List<Recipe>>(new ArrayList<Recipe>(), HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<Recipe>>(recipes, HttpStatus.OK);
    }

    @GetMapping("/getById")
    ResponseEntity<Recipe> getRecipes(@RequestParam long recipeId) {
        Recipe recipe = recipeService.getRecipe(recipeId);

        if(recipe == null)
            return new ResponseEntity<Recipe>(new Recipe(), HttpStatus.NO_CONTENT);
        return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
    }

    @PostMapping("/add")
    ResponseEntity<String> addRecipe(@RequestParam long userId,
                                     @RequestBody Recipe recipe) {
        if(recipe == null)
            return new ResponseEntity<String>("Bad request!", HttpStatus.BAD_REQUEST);

        recipe.setUserId(userId);
        Recipe response = recipeService.addRecipe(recipe);

        if(response == null)
            return new ResponseEntity<String>("Server error!", HttpStatus.INTERNAL_SERVER_ERROR);

        User user = userService.getUser(userId);
        Set<Long> recipeIdList = user.getRecipeIdList();
        if(recipeIdList == null)
            recipeIdList = new HashSet<>();
        recipeIdList.add(response.getId());
        user.setRecipeIdList(recipeIdList);
        userService.editUser(user);

        return new ResponseEntity<String>("Added Recipe successfully!", HttpStatus.OK);
    }

    @PutMapping("/edit")
    ResponseEntity<String> editRecipe(@RequestParam long recipeId,
                                      @RequestParam(required = false) String keywords,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) String imageUrl,
                                      @RequestBody(required = false) String recipeText) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);

        if(keywords != null)
            recipe.setKeywords(keywords);
        if(title != null)
            recipe.setTitle(title);
        if(imageUrl != null)
            recipe.setImageUrl(imageUrl);
        if(recipeText != null)
            recipe.setRecipeText(recipeText);

        String response = recipeService.updateRecipe(recipe);
        if(response == null)
            return new ResponseEntity<String>("Server error!", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @PutMapping("/fav")
    ResponseEntity<String> favRecipe(@RequestParam long userId,
                                     @RequestParam long recipeId) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);


        User user = userService.getUser(userId);
        Set<Long> favRecipeIdList = user.getFavRecipeIdList();
        if(favRecipeIdList == null)
            favRecipeIdList = new HashSet<Long>();
        favRecipeIdList.add(recipeId);
        user.setFavRecipeIdList(favRecipeIdList);
        userService.editUser(user);

        return new ResponseEntity<String>("Added Recipe to favourites successfully!", HttpStatus.OK);
    }

    @PutMapping("/unfav")
    ResponseEntity<String> unfavRecipe(@RequestParam long userId,
                                     @RequestParam long recipeId) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);


        User user = userService.getUser(userId);
        Set<Long> favRecipeIdList = user.getFavRecipeIdList();
        if(favRecipeIdList == null)
            favRecipeIdList = new HashSet<Long>();
        favRecipeIdList.remove(recipeId);
        user.setFavRecipeIdList(favRecipeIdList);
        userService.editUser(user);

        return new ResponseEntity<String>("Removed Recipe from favourites successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteRecipe(@RequestParam long recipeId) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);

        long userId = recipe.getUserId();

        User user = userService.getUser(userId);
        Set<Long> recipeIdList = user.getRecipeIdList();
        if(recipeIdList == null)
            recipeIdList = new HashSet<>();
        recipeIdList.remove(recipeId);
        user.setRecipeIdList(recipeIdList);
        userService.editUser(user);

        List<User> users = userService.getUsers();
        users.stream().forEach(u -> {
            Set<Long> favRecipeIdList = u.getFavRecipeIdList();
            if(favRecipeIdList == null)
                favRecipeIdList = new HashSet<>();
            favRecipeIdList.remove(recipeId);
            u.setFavRecipeIdList(favRecipeIdList);
            userService.editUser(u);
        });


        recipeService.deleteRecipe(recipeId);

        return new ResponseEntity<String>("Deleted Recipe successfully!", HttpStatus.OK);
    }

    @PutMapping("/comments/add")
    ResponseEntity<String> addComment(@RequestParam long recipeId,
                                      @RequestParam long commenterId,
                                      @RequestBody String comment) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);

        List<List<String>> comments = recipe.getComments();

        if(comments == null)
            comments = new ArrayList<>();

        List<String> newComment = new ArrayList<>();
        newComment.add(String.valueOf(commenterId));
        newComment.add(comment);

        comments.add(newComment);
        recipe.setComments(comments);
        recipeService.updateRecipe(recipe);

        return new ResponseEntity<String>("Added comment successfully!", HttpStatus.OK);
    }

    @PutMapping("/comments/edit")
    ResponseEntity<String> editComment(@RequestParam long recipeId,
                                       @RequestParam long commenterId,
                                       @RequestBody String commentToEdit) {

        Recipe recipe = recipeService.getRecipe(recipeId);
        if(recipe == null)
            return new ResponseEntity<String>("No such recipe!", HttpStatus.INTERNAL_SERVER_ERROR);

        final String oldComment = commentToEdit.substring(0, commentToEdit.lastIndexOf(';'));
        final String newComment = commentToEdit.substring(commentToEdit.lastIndexOf(';') + 1);


        List<List<String>> comments = recipe.getComments();

        if(comments == null)
            comments = new ArrayList<>();


        for(var c : comments) {
            if(c.getFirst().equalsIgnoreCase(String.valueOf(commenterId)))
                if(c.get(1).equalsIgnoreCase(oldComment))
                {
                    comments.remove(c);
                    c.set(1,newComment);
                    comments.add(c);
                    break;
                }
        };


        recipe.setComments(comments);
        recipeService.updateRecipe(recipe);

        return new ResponseEntity<String>("Edited comment successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/comments/delete")
    ResponseEntity<String> deleteComment(@RequestParam long recipeId,
                                        @RequestParam long commenterId,
                                        @RequestParam int commentIndex) {

        Recipe recipe = recipeService.getRecipe(recipeId);

        List<List<String>> comments = recipe.getComments();

        if(comments == null)
            comments = new ArrayList<>();

        try {
            comments.remove(commentIndex);
        } catch (Exception e) {
            return new ResponseEntity<String>("Could not delete!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        recipe.setComments(comments);
        recipeService.updateRecipe(recipe);

        return new ResponseEntity<String>("Deleted comment successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/comments/deleteAll")
    ResponseEntity<String> deleteAllComments(@RequestParam long recipeId,
                                             @RequestParam long commenterId) {

        Recipe recipe = recipeService.getRecipe(recipeId);

        List<List<String>> comments = recipe.getComments();

        if(comments == null)
            comments = new ArrayList<>();

        comments.remove(commenterId);
        recipe.setComments(comments);
        recipeService.updateRecipe(recipe);

        return new ResponseEntity<String>("Deleted all comments successfully!", HttpStatus.OK);
    }

    @DeleteMapping("comments/clear")
    ResponseEntity<String> clearComments(@RequestParam long recipeId) {

        Recipe recipe = recipeService.getRecipe(recipeId);

        recipe.setComments(new ArrayList<>());
        recipeService.updateRecipe(recipe);

        return new ResponseEntity<String>("Cleared comments successfully!", HttpStatus.OK);
    }
}

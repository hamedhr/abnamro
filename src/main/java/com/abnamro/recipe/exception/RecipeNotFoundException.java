package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecipeNotFoundException extends BaseException {
    public RecipeNotFoundException() {
        super("RecipeNotFound");
    }

    public RecipeNotFoundException(Long recipeId) {
        super("Recipe Not found with id:" + recipeId);
    }
}

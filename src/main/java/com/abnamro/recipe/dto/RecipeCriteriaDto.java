package com.abnamro.recipe.dto;

import com.abnamro.recipe.model.DishType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.stream.Stream;

public class RecipeCriteriaDto {
    private DishType dishType;
    private Integer servings;
    private String includeIngredients;
    private String excludeIngredients;
    private String instructions;

    @JsonIgnore
    public boolean isParamsEmpty() {
        return Stream.of(
                dishType,
                servings,
                includeIngredients
        ).allMatch(Objects::isNull);
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getIncludeIngredients() {
        return includeIngredients;
    }

    public void setIncludeIngredients(String includeIngredients) {
        this.includeIngredients = includeIngredients;
    }

    public String getExcludeIngredients() {
        return excludeIngredients;
    }

    public void setExcludeIngredients(String excludeIngredients) {
        this.excludeIngredients = excludeIngredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }


}

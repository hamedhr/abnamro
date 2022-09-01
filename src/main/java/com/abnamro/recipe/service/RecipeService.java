package com.abnamro.recipe.service;

import com.abnamro.recipe.dto.RecipeCriteriaDto;
import com.abnamro.recipe.dto.RecipeCreateDto;
import com.abnamro.recipe.dto.RecipeDto;
import com.abnamro.recipe.dto.RecipeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    List<RecipeDto> getAll();

    RecipeDto createNew(RecipeCreateDto recipe);

    RecipeDto update(RecipeUpdateDto recipe);

    RecipeDto getById(Long recipeId);

    void deleteById(Long recipeId);

    Page<RecipeDto> find(RecipeCriteriaDto criteria, Pageable pageable);
}

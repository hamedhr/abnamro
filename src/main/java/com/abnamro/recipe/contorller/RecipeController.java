package com.abnamro.recipe.contorller;

import com.abnamro.recipe.dto.RecipeCreateDto;
import com.abnamro.recipe.dto.RecipeCriteriaDto;
import com.abnamro.recipe.dto.RecipeDto;
import com.abnamro.recipe.dto.RecipeUpdateDto;
import com.abnamro.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;
    private static final String DEFAULT_ORDER_BY = "createdAt";

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeDto> getAll() {
        return recipeService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto create(@RequestBody RecipeCreateDto recipe) {
        return recipeService.createNew(recipe);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto create(@RequestBody RecipeUpdateDto recipe) {
        return recipeService.update(recipe);
    }

    @GetMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto get(@PathVariable Long recipeId) {
        return recipeService.getById(recipeId);
    }

    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long recipeId) {
        recipeService.deleteById(recipeId);
    }

    @GetMapping(value = "/find")
    @ResponseStatus(HttpStatus.OK)
    public Page<RecipeDto> find(RecipeCriteriaDto recipe,
                                @Parameter @RequestParam(required = false, defaultValue = "1") Integer page,
                                @Parameter @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return recipeService.find(recipe, createPageRequest(page, pageSize, DEFAULT_DIRECTION, DEFAULT_ORDER_BY));
    }

    private PageRequest createPageRequest(Integer page, Integer pageSize, Sort.Direction direction, String orderBy) {
        return PageRequest.of(page - 1, pageSize, Sort.by(direction, orderBy));
    }
}

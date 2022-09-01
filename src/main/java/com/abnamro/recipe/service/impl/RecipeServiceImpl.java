package com.abnamro.recipe.service.impl;

import com.abnamro.recipe.dto.RecipeCreateDto;
import com.abnamro.recipe.dto.RecipeCriteriaDto;
import com.abnamro.recipe.dto.RecipeDto;
import com.abnamro.recipe.dto.RecipeUpdateDto;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.mapper.RecipeMapper;
import com.abnamro.recipe.model.QIngredient;
import com.abnamro.recipe.model.QRecipe;
import com.abnamro.recipe.model.Recipe;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.recipe.service.RecipeService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    private static final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);
    public static final QRecipe RECIPE_ENTITY = QRecipe.recipe;
    public static final QIngredient INGREDIENT_ENTITY = QIngredient.ingredient;
    private final JPAQueryFactory queryFactory;

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(JPAQueryFactory queryFactory, RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.queryFactory = queryFactory;
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public List<RecipeDto> getAll() {
        log.info("getAll Recipes called");
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDto createNew(RecipeCreateDto recipe) {
        log.info("createNew Recipe called");
        final Recipe saved = recipeRepository.save(recipeMapper.toEntity(recipe));
        return recipeMapper.toDto(saved);
    }

    @Override
    public RecipeDto update(RecipeUpdateDto updateRecipe) {
        log.info("createNew Recipe called");
        Recipe recipe = getRecipeById(updateRecipe.getId());
        recipeMapper.toEntity(updateRecipe, recipe);
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

    @Override
    public RecipeDto getById(Long recipeId) {
        log.info("getById Recipe called({})", recipeId);
        Assert.notNull(recipeId, "recipeId must not be null");
        return recipeMapper.toDto(getRecipeById(recipeId));
    }

    @Override
    public void deleteById(Long recipeId) {
        log.info("deleteById Recipe called({})", recipeId);
        Assert.notNull(recipeId, "recipeId must not be null");
        recipeRepository.deleteById(recipeId);
        //TODO hamed: We can soft delete it instead!
    }

    @Override
    public Page<RecipeDto> find(RecipeCriteriaDto criteria, Pageable pageable) {
        log.info("find criteria:{} | pageable:{}", criteria, pageable);

        final JPAQuery<Recipe> result = createQuery(criteria, pageable);
        final List<RecipeDto> collect = result.stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, result.stream().count());
    }

    private JPAQuery<Recipe> createQuery(RecipeCriteriaDto criteria, Pageable pageable) {
        return queryFactory.select(RECIPE_ENTITY).from(RECIPE_ENTITY)
                .join(INGREDIENT_ENTITY).on(INGREDIENT_ENTITY.id.eq(RECIPE_ENTITY.id))
                .where(createWhereClause(criteria))
                .orderBy(RECIPE_ENTITY.createdAt.desc()) //TODO hamed:  create mapping for orderBys
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());
    }

    private Predicate createWhereClause(RecipeCriteriaDto dto) {
        if (dto.isParamsEmpty()) {
            throw new IllegalArgumentException("Provide at least one none null param"); //TODO hamed: Create custom Exception
        }
        BooleanBuilder clause = new BooleanBuilder();
        if (dto.getDishType() != null) {
            clause.and(RECIPE_ENTITY.dishType.eq(dto.getDishType()));
        }
        if (dto.getServings() != null) {
            clause.and(RECIPE_ENTITY.servings.eq(dto.getServings()));
        }
        if (dto.getInstructions() != null) {
            clause.and(RECIPE_ENTITY.instructions.contains(dto.getInstructions()));
        }
        if (dto.getIncludeIngredients() != null) {
            clause.and(INGREDIENT_ENTITY.name.equalsIgnoreCase(dto.getIncludeIngredients()));
        }
        if (dto.getExcludeIngredients() != null) {
            clause.and(INGREDIENT_ENTITY.name.notEqualsIgnoreCase(dto.getExcludeIngredients()));
        }
        return clause;
    }

    private Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));
    }
}

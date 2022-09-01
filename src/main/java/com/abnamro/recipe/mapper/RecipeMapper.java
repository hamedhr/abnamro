package com.abnamro.recipe.mapper;

import com.abnamro.recipe.dto.RecipeCreateDto;
import com.abnamro.recipe.dto.RecipeDto;
import com.abnamro.recipe.dto.RecipeUpdateDto;
import com.abnamro.recipe.model.Recipe;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeDto toDto(Recipe recipe);

    Recipe toEntity(RecipeCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(RecipeUpdateDto dto, @MappingTarget Recipe entity);
}

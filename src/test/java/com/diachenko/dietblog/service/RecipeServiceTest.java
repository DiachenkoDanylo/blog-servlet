package com.diachenko.dietblog.service;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.recipe.RecipeDao;
import com.diachenko.dietblog.exception.DatabaseException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeDao recipeDao;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe testRecipe;
    private AppUser testAppUser;

    @BeforeEach
    void setUp() {
        testAppUser = new AppUser(1, "test@example.com", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 10, 10, 10, 10, 10), "image.jpg");
        testRecipe = new Recipe(1, "test title", "test description", 100, testAppUser, testAppUser.getCreatedAt(), "recipeImage.jpg");
    }

    @Test
    void test_save_newRecipe_Success() {
        when(recipeDao.createRecipe(testRecipe)).thenReturn(testRecipe);

        Recipe savedRecipe = recipeDao.createRecipe(testRecipe);

        assertEquals(savedRecipe, testRecipe);
    }

    @Test
    void test_save_Recipe_Exception() {
        when(recipeDao.createRecipe(testRecipe)).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.save(testRecipe));
    }

    @Test
    void test_findById_Success() {
        when(recipeDao.getRecipeById(1)).thenReturn(Optional.of(testRecipe));
        Recipe foundRecipe = recipeService.findById(1);
        assertEquals(testRecipe, foundRecipe);
    }

    @Test
    void test_findById_DatabaseException() {
        when(recipeDao.getRecipeById(1)).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.findById(1));
    }

    @Test
    void test_findAll_Success() {
        List<Recipe> recipes = Arrays.asList(testRecipe);
        when(recipeDao.getAllRecipes()).thenReturn(recipes);
        List<Recipe> foundRecipes = recipeService.findAll();
        assertEquals(recipes, foundRecipes);
    }

    @Test
    void test_findAll_DatabaseException() {
        when(recipeDao.getAllRecipes()).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.findAll());
    }

    @Test
    void test_deleteById_Success() {
        when(recipeDao.deleteRecipe(1)).thenReturn(true);
        assertTrue(recipeService.deleteById(1));
    }

    @Test
    void test_deleteById_DatabaseException() {
        when(recipeDao.deleteRecipe(1)).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.deleteById(1));
    }

    @Test
    void test_findByTitle_Success() {
        when(recipeDao.getRecipeByTitle("test title")).thenReturn(Optional.of(testRecipe));
        Recipe foundRecipe = recipeService.findByTitle("test title");
        assertEquals(testRecipe, foundRecipe);
    }

    @Test
    void test_findByTitle_DatabaseException() {
        when(recipeDao.getRecipeByTitle("test title")).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.findByTitle("test title"));
    }

    @Test
    void test_updateRecipeById_Success() {
        when(recipeDao.getRecipeById(1)).thenReturn(Optional.of(testRecipe));
        doNothing().when(recipeDao).updateRecipe(testRecipe);
        Recipe updatedRecipe = recipeService.updateRecipeById(testRecipe);
        assertEquals(testRecipe, updatedRecipe);
    }

    @Test
    void test_updateRecipeById_DatabaseException() {
        when(recipeDao.getRecipeById(1)).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.updateRecipeById(testRecipe));
    }

    @Test
    void test_findAllByUserId_Success() {
        List<Recipe> recipes = Arrays.asList(testRecipe);
        when(recipeDao.getAllRecipesByUserId(1)).thenReturn(recipes);
        List<Recipe> foundRecipes = recipeService.findAllByUserId(1);
        assertEquals(recipes, foundRecipes);
    }

    @Test
    void test_findAllByUserId_DatabaseException() {
        when(recipeDao.getAllRecipesByUserId(1)).thenThrow(new DatabaseException("error"));
        assertThrows(ServiceException.class, () -> recipeService.findAllByUserId(1));
    }
}

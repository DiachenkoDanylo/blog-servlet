package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.utils.DatabaseConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class ShowTest {

    private final AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private final Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

    private Show show;

    @Mock
    private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private ServletConfig servletConfig;
    @Mock private RequestDispatcher requestDispatcher;
    @Mock private RecipeServiceImpl recipeService;

    @AfterAll
    static void tearDown() {
        DatabaseConfig.closeDataSource();
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        DatabaseConfig.setTestDataSource(mockDataSource); // Подмена на тестовую БД
        show = new Show();

        // Initialize the servlet (calls init())
        show.init(servletConfig);

        // Inject mocks using reflection

        setField(show, "recipeService", recipeService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void test_Get_ById_Success() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(recipeService.findById(recipe.getId())).thenReturn(recipe);
        when(request.getRequestDispatcher("/recipe/recipeShow.jsp")).thenReturn(requestDispatcher);

        show.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute("recipe", recipe);
    }

    @Test
    void test_Get_ById_ServiceException() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(recipeService.findById(recipe.getId())).thenThrow(new ServiceException("error"));

        show.doGet(request, response);

        verify(response).sendRedirect("/recipe/recipeShow.jsp?error=Error loading recipe. Please try again.");

    }
    @Test
    void test_Get_ById_NumberFormatException() throws Exception {
        when(request.getParameter("id")).thenReturn("asd");

        show.doGet(request, response);

        verify(response).sendRedirect("/recipe/recipeShow.jsp?error=Invalid recipe ID.");
    }

    @Test
    void test_Get_All_Success() throws Exception {
        when(request.getParameter("id")).thenReturn(null);
        when(recipeService.findAll()).thenReturn(List.of(recipe));
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        show.doGet(request, response);

        verify(request).setAttribute("recipes", List.of(recipe));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void test_Get_All_ServiceException() throws Exception {
        when(request.getParameter("id")).thenReturn(null);
        when(recipeService.findAll()).thenThrow(new ServiceException("error"));

        show.doGet(request, response);

        verify(response).sendRedirect("/index.jsp?error=Error loading recipes. Please try again later.");
    }
}

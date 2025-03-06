package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.service.image.LocalImageStorageService;
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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class EditTest {

    private final AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private final Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

    private Edit edit;

    @Mock private RecipeServiceImpl recipeService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private ServletConfig servletConfig;
    @Mock private RequestDispatcher requestDispatcher;
    @Mock private AuthenticationService authenticationService;
    @Mock private LocalImageStorageService storageService;

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
        edit = new Edit();

        // Initialize the servlet (calls init())
        edit.init(servletConfig);

        // Inject mocks using reflection

        setField(edit, "recipeService", recipeService);
        setField(edit, "storageService", storageService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void test_Get_Success() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when(recipeService.findById(recipe.getId())).thenReturn(recipe);
        when(request.getRequestDispatcher("/recipe/edit.jsp")).thenReturn(requestDispatcher);

        edit.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute("recipe", recipe);
    }

    @Test
    void test_Get_ServiceException() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when(recipeService.findById(recipe.getId())).thenThrow(new ServiceException("error"));
        when(request.getRequestDispatcher("/recipe/edit.jsp")).thenReturn(requestDispatcher);

        edit.doGet(request, response);

        verify(response).sendRedirect("/recipeShow.jsp?id="+recipe.getId());
    }

    @Test
    void test_Get_RecipeNotFoundException() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when(recipeService.findById(recipe.getId())).thenThrow(new RecipeNotFoundException("not found "));
        when(request.getRequestDispatcher("/recipe/edit.jsp")).thenReturn(requestDispatcher);

        edit.doGet(request, response);

        verify(response).sendRedirect("/recipeShow.jsp?error=Recipe not found for editing");
    }

    @Test
    void test_Get_NumberFormatException() throws Exception {
        when(request.getParameter("id")).thenReturn("asd");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);

        edit.doGet(request, response);

        verify(response).sendRedirect("/recipeShow.jsp?error=Invalid recipe ID for editing");
    }

    @Test
    void test_Post_Success() throws Exception {
//        int id = 2;
        String title = "title2";
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(recipe.getDescription());
        when(request.getParameter("calories")).thenReturn(String.valueOf(recipe.getCalories()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);

        when(recipeService.findById(recipe.getId())).thenReturn(recipe);
        edit.doPost(request, response);

        verify(response).sendRedirect("/recipes?id="+recipe.getId());
    }

    @Test
    void test_Post_ServiceException() throws Exception {
        String title = "title2";
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(recipe.getDescription());
        when(request.getParameter("calories")).thenReturn(String.valueOf(recipe.getCalories()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);

        when(recipeService.findById(recipe.getId())).thenThrow(new ServiceException("error"));

        edit.doPost(request, response);

        verify(response).sendRedirect("/recipes?id="+recipe.getId());
    }

    @Test
    void test_Post_RecipeNotFoundException() throws Exception {
        String title = "title2";
        when(request.getParameter("id")).thenReturn(String.valueOf(recipe.getId()));
        when(request.getParameter("title")).thenReturn(title);
        when(request.getParameter("description")).thenReturn(recipe.getDescription());
        when(request.getParameter("calories")).thenReturn(String.valueOf(recipe.getCalories()));
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);

        when(recipeService.findById(recipe.getId())).thenThrow(new RecipeNotFoundException("not found"));

        edit.doPost(request, response);

        verify(response).sendRedirect("/recipes?id="+recipe.getId());

    }
}

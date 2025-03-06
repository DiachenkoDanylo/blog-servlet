package com.diachenko.dietblog.servlet.author;
/*  diet-blog
    24.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.AppUserServiceImpl;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class AuthorTest {

    private Author author;

    @Mock private RecipeServiceImpl recipeService;
    @Mock private AppUserServiceImpl appUserService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private ServletConfig servletConfig;
    @Mock private RequestDispatcher requestDispatcher;

    private AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

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
        author = new Author();

        // Initialize the servlet (calls init())
        author.init(servletConfig);

        // Inject mocks using reflection
        setField(author, "appUserService", appUserService);
        setField(author, "recipeService", recipeService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testAllAuthors_Get_Success() throws Exception {
        when(request.getRequestDispatcher("/recipe/recipeAuthor.jsp")).thenReturn(requestDispatcher);
        when(appUserService.findAll()).thenReturn(List.of(appUser));

        author.doGet(request, response);

        verify(request).setAttribute("authorsList", List.of(appUser));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testAllAuthors_Get_ServiceException() throws Exception {
        when(appUserService.findAll()).thenThrow(new ServiceException("Database error occurred while fetching authors "));

        author.doGet(request, response);

        verify(response).sendRedirect("/index.jsp?error=Error loading authors. Please try again later.");
    }


    @Test
    void testAuthorById_Get_Success() throws Exception {
        int authorId = 1;
        when(request.getParameter("id")).thenReturn(String.valueOf(authorId));

        when(request.getRequestDispatcher("/recipe/recipeAuthorShow.jsp")).thenReturn(requestDispatcher);
        when(recipeService.findAllByUserId(authorId)).thenReturn(List.of(recipe));
        when(appUserService.findById(authorId)).thenReturn(appUser);

        author.doGet(request, response);

        verify(request).setAttribute("recipes", List.of(recipe));
        verify(request).setAttribute("author", appUser);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testAuthorById_Get_RecipeNotFoundException() throws Exception {
        int authorId = 1;
        when(request.getRequestDispatcher("/recipe/recipeAuthorShow.jsp")).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn(String.valueOf(authorId));
        when(recipeService.findAllByUserId(authorId)).thenThrow(new RecipeNotFoundException("Recipes by author with id :" + authorId + " not found"));
        when(appUserService.findById(authorId)).thenReturn(appUser);

        author.doGet(request, response);

        verify(request).setAttribute("author", appUser);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testAuthorById_Get_ServiceException() throws Exception {
        int authorId = 1;
        when(request.getRequestDispatcher("/recipe/recipeAuthorShow.jsp")).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn(String.valueOf(authorId));
        when(recipeService.findAllByUserId(authorId)).thenThrow(new ServiceException("Service Exception"));

        author.doGet(request, response);

        verify(response).sendRedirect("/recipe/recipeAuthor.jsp?error=Error loading author. Please try again.");
    }

    @Test
    void testAuthorById_Get_NumberFormatException() throws Exception {
        when(request.getParameter("id")).thenReturn("a");

        author.doGet(request, response);

        verify(response).sendRedirect("/recipe/recipeAuthor.jsp?error=Invalid author ID");
    }

    @Test
    void testAuthorById_Get_UserNotFoundException() throws Exception {
        int authorId = 1;
        when(request.getRequestDispatcher("/recipe/recipeAuthorShow.jsp")).thenReturn(requestDispatcher);
        when(request.getParameter("id")).thenReturn(String.valueOf(authorId));
        when(recipeService.findAllByUserId(authorId)).thenReturn(List.of(recipe));
        when(appUserService.findById(authorId)).thenThrow(new UserNotFoundException("User with ID " + authorId + " not found"));

        author.doGet(request, response);

        verify(response).sendRedirect("/recipe/recipeAuthor.jsp?error=User with this author ID not found");
    }

}

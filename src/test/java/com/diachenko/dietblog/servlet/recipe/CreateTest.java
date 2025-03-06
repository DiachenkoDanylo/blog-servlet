package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.service.image.ImageStorageFactory;
import com.diachenko.dietblog.service.image.LocalImageStorageService;
import com.diachenko.dietblog.servlet.author.Cabinet;
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
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class CreateTest {

    private final AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private final Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

    private Create create;

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
        create = new Create();

        // Initialize the servlet (calls init())
        create.init(servletConfig);

        // Inject mocks using reflection

        setField(create, "recipeService", recipeService);
        setField(create, "storageService", storageService);
        setField(create, "authenticationService", authenticationService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGet_Success() throws  Exception {
        when(request.getRequestDispatcher("/recipe/recipeCreate.jsp")).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);

        create.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testGet_UnauthorizedAccessException() throws  Exception {
        when(request.getRequestDispatcher("/recipe/recipeCreate.jsp")).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when(authenticationService.isUserAuthenticateValid(appUser)).thenThrow( new UnauthorizedAccessException("unauthorized access"));

        create.doGet(request, response);

        verify(response).sendRedirect("/login");
    }

    private final String TITLE = "testing Title";
    private final String DESCRIPTION = "testing Description";
    private final String CALORIES = "100";

    @Test
    void testPost_Success_RecipeNotFoundException() throws  Exception {
        when(request.getParameter("title")).thenReturn(TITLE);
        when(request.getParameter("description")).thenReturn(DESCRIPTION);
        when(request.getParameter("calories")).thenReturn(CALORIES);
        when(request.getSession()).thenReturn(session);
        when((AppUser) session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);

        when(request.getPart("image")).thenReturn(filePart);
        when(recipeService.findByTitle(TITLE)).thenThrow(new RecipeNotFoundException("not found with title"));
        when(filePart.getInputStream()).thenReturn(new ByteArrayInputStream("dummy image content".getBytes()));
        when(filePart.getSubmittedFileName()).thenReturn("test-image.jpg");
        when(storageService.saveImagePart(filePart)).thenReturn("uploads/test-image.jpg");

        create.doPost(request, response);

        verify(response).sendRedirect("/recipes");
    }

    @Test
    void testPost_ServiceException() throws  Exception {
        when(request.getParameter("title")).thenReturn(TITLE);
        when(request.getParameter("description")).thenReturn(DESCRIPTION);
        when(request.getParameter("calories")).thenReturn(CALORIES);
        when(request.getSession()).thenReturn(session);
        when((AppUser) session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);
        when(request.getPart("image")).thenReturn(filePart);
        when(recipeService.findByTitle(TITLE)).thenThrow(new ServiceException("service exception"));

        create.doPost(request, response);

        verify(response).sendRedirect("/recipe/reciteCreate.jsp?error=Error during adding recipe");
    }

    @Test
    void testPost_UnauthorizedAccessException() throws  Exception {
        when(request.getParameter("title")).thenReturn(TITLE);
        when(request.getParameter("description")).thenReturn(DESCRIPTION);
        when(request.getParameter("calories")).thenReturn(CALORIES);
        when(request.getSession()).thenReturn(session);
        when((AppUser) session.getAttribute("user")).thenReturn(appUser);
        Part filePart = mock(Part.class);
        when(request.getPart("image")).thenReturn(filePart);
        when(authenticationService.isUserAuthenticateValid(appUser)).thenThrow(new UnauthorizedAccessException("unauthorized access"));

        create.doPost(request, response);

        verify(response).sendRedirect("/login");
    }


}

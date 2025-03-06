package com.diachenko.dietblog.servlet;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

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
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class MainTest {

    private final AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private final Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

    private Main main;

    @Mock
    private RecipeServiceImpl recipeService;
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
        main = new Main();

        // Initialize the servlet (calls init())
        main.init(servletConfig);

        // Inject mocks using reflection

        setField(main, "recipeService", recipeService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGet_Success() throws Exception {
        when(recipeService.findAll()).thenReturn(List.of(recipe));
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        main.doGet(request, response);

        verify(request).setAttribute("recipes", List.of(recipe));
        verify(requestDispatcher).forward(request, response);

    }

    @Test
    void testGet_ServiceException() throws Exception {
        when(recipeService.findAll()).thenThrow(new ServiceException("error"));
        when(request.getRequestDispatcher("/index.jsp?error=Server failed")).thenReturn(requestDispatcher);

        main.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }
}

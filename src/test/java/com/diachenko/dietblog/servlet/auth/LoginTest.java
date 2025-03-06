package com.diachenko.dietblog.servlet.auth;
/*  diet-blog
    24.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.AuthenticationService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginTest {

    private Login loginServlet;

    @Mock private AppUserServiceImpl appUserService;
    @Mock private AuthenticationService authService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private ServletConfig servletConfig;
    @Mock private RequestDispatcher requestDispatcher;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Создаем мок DataSource и Connection
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        DatabaseConfig.setTestDataSource(mockDataSource); // Подмена на тестовую БД

        loginServlet = new Login();
        // Initialize the servlet (calls init())
        loginServlet.init(servletConfig);

        // Inject mocks using reflection
        setField(loginServlet, "appUserService", appUserService);
        setField(loginServlet, "authService", authService);
    }

    @AfterAll
    static void tearDown() {
        DatabaseConfig.closeDataSource();
    }


    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }


    @Test
     void testLogin_Get_Success() throws Exception {
        when(request.getRequestDispatcher("/auth/login.jsp")).thenReturn(requestDispatcher);
        loginServlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }


    @Test
    void testLogin_Post_Success() throws Exception {
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(appUserService.findByEmail("test@example.com")).thenReturn(new AppUser());
        when(authService.authenticate(eq("password123"), any(AppUser.class))).thenReturn(true);
        when(request.getSession()).thenReturn(session);

        loginServlet.doPost(request, response);

        verify(session).setAttribute(eq("user"), any(AppUser.class));
        verify(response).sendRedirect("recipes");
    }

    @Test
    void testLogin_Post_InvalidPassword() throws Exception {
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(appUserService.findByEmail("test@example.com")).thenReturn(new AppUser());
        when(authService.authenticate(eq("wrongpassword"), any(AppUser.class))).thenReturn(false);

        loginServlet.doPost(request, response);

        verify(response).sendRedirect("/auth/login.jsp?error=Invalid password or email");
    }

    @Test
    void testLogin_Post_UserNotFound() throws Exception {
        when(request.getParameter("email")).thenReturn("notfound@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(appUserService.findByEmail("notfound@example.com")).thenThrow(new UserNotFoundException("User not found"));

        loginServlet.doPost(request, response);

        verify(response).sendRedirect("/auth/login.jsp?error=User not found");
    }

    @Test
    void testLogin_Post_ServiceException() throws Exception {
        when(request.getParameter("email")).thenReturn("exception@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(appUserService.findByEmail("exception@example.com")).thenThrow(new ServiceException("Unexpected error during login"));

        loginServlet.doPost(request,response);

        verify(response).sendRedirect("/auth/login.jsp?error=Server error");
    }
}

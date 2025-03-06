package com.diachenko.dietblog.servlet.auth;
/*  diet-blog
    24.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.DuplicateObjectException;
import com.diachenko.dietblog.exception.ServiceException;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterTest {


    private Register register;

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
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        DatabaseConfig.setTestDataSource(mockDataSource); // Подмена на тестовую БД
        register = new Register();

        // Initialize the servlet (calls init())
        register.init(servletConfig);

        // Inject mocks using reflection
        setField(register, "appUserService", appUserService);
        setField(register, "authService", authService);
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
    void testRegister_Get_Success() throws Exception {
        when(request.getRequestDispatcher("auth/register.jsp")).thenReturn(requestDispatcher);
        register.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testRegister_Post_Success() throws Exception {
        when(authService.convertPassword("testpass")).thenReturn("12345");
        AppUser appUserSaved = AppUser.builder()
                .id(1)
                .email("test@example.com")
                .username("test")
                .image("uploads/default_icon.jpg")
                .passwordHash("12345")
                .role("user")
                .createdAt(LocalDateTime.of(2024,12,12,12,12,12))
                .build();
        when(request.getParameter("username")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(appUserService.save(any(AppUser.class))).thenReturn(appUserSaved);
        when(request.getSession()).thenReturn(session);

        register.doPost(request, response);

        verify(session).setAttribute("user", appUserSaved);
        verify(response).sendRedirect("/");
    }

    @Test
    void testRegister_Post_DuplicateException() throws Exception {
        when(authService.convertPassword("testpass")).thenReturn("12345");
        when(request.getParameter("username")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(appUserService.save(any(AppUser.class))).thenThrow(new DuplicateObjectException("User with email ::  already exists."));

        register.doPost(request,response);

        verify(response).sendRedirect("auth/login.jsp?error = Please try login! Email was already in use");
    }

    @Test
    void testRegister_Post_ServiceException() throws Exception {
        when(authService.convertPassword("testpass")).thenReturn("12345");
        when(request.getParameter("username")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(appUserService.save(any(AppUser.class))).thenThrow(new ServiceException("Database error occurred while checking user existence"));

        register.doPost(request,response);

        verify(response).sendRedirect("auth/register.jsp?error = Please try again later!");
    }
}

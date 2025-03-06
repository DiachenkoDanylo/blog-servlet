package com.diachenko.dietblog.servlet.auth;
/*  diet-blog
    24.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.utils.DatabaseConfig;
import com.diachenko.dietblog.utils.IPService;
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
import java.io.IOException;
import java.sql.Connection;

import static org.mockito.Mockito.*;

class LogoutTest {

    private Logout logout;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private ServletConfig servletConfig;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        DatabaseConfig.setTestDataSource(mockDataSource); // Подмена на тестовую БД
        logout = new Logout();

        // Initialize the servlet (calls init())
        logout.init(servletConfig);
    }

    @Test
    void testLogout_Get_SuccessLogout() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(AppUser.builder().email("test@mail.com").build());

        logout.doGet(request,response);

        verify(response).sendRedirect("recipes");
        verify(session, times(1)).invalidate();
    }

    @Test
    void testLogout_Get_ExceptionLogout() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenThrow(new ServiceException("Error"));

        logout.doGet(request,response);

        verify(response).sendRedirect("index.jsp");
        verify(session, times(1)).invalidate();
    }
}

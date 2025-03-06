package com.diachenko.dietblog.servlet.author;
/*  diet-blog
    03.03.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.service.image.ImageStorageService;
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

public class EditTest {

    private final AppUser appUser = new AppUser(1, "test", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 12, 12, 12, 12, 12), "uploads/default_icon.jpg");
    private final Recipe recipe = new Recipe(2, "test", "test description", 100, appUser, LocalDateTime.of(2022, 11, 11, 11, 11, 0), "uploads/title.jpg");

    private Edit edit;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private ServletConfig servletConfig;
    @Mock private RequestDispatcher requestDispatcher;
    @Mock private LocalImageStorageService storageService;
    @Mock private AuthenticationService authenticationService;
    @Mock private AppUserServiceImpl appUserService;

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

        setField(edit, "authenticationService", authenticationService);
        setField(edit, "appUserService", appUserService);
        setField(edit, "storageService", storageService);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void test_Get_Success() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when(request.getRequestDispatcher("/cabinet-edit.jsp")).thenReturn(requestDispatcher);

        edit.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }


    @Test
    void test_Post_Success() throws Exception {
        String updateUsername = "username2";
        when(request.getParameter("id")).thenReturn(String.valueOf(appUser.getId()));
        when(request.getParameter("username")).thenReturn(appUser.getUsername());
        when(request.getParameter("email")).thenReturn(appUser.getEmail());
        Part filePart = mock(Part.class);
        when(appUserService.findById(appUser.getId())).thenReturn(appUser);
        when(request.getPart("image")).thenReturn(filePart);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when((AppUser) session.getAttribute("user")).thenReturn(appUser);
        when(storageService.saveImagePart(filePart)).thenReturn("uploads/imagename.jpg");
        AppUser appUser2 = appUser;
        appUser2.setUsername(updateUsername);
        when(appUserService.updateAppUserById(any())).thenReturn(appUser2);

        edit.doPost(request, response);

        verify(response).sendRedirect("/cabinet");
        verify(session).setAttribute("user",appUser2);
    }

    @Test
    void test_Post_ServiceException() throws Exception {
        when(request.getParameter("id")).thenReturn(String.valueOf(appUser.getId()));
        when(request.getParameter("username")).thenReturn(appUser.getUsername());
        when(request.getParameter("email")).thenReturn(appUser.getEmail());
        Part filePart = mock(Part.class);
        when(request.getPart("image")).thenReturn(filePart);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(appUser);
        when((AppUser) session.getAttribute("user")).thenReturn(appUser);
        when(appUserService.findById(appUser.getId())).thenThrow(new ServiceException("error"));

        edit.doPost(request, response);

        verify(response).sendRedirect("/cabinet-edit.jsp?error=Server error");
    }

}

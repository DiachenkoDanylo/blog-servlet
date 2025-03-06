package com.diachenko.dietblog.service;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.utils.DatabaseConfig;
import com.diachenko.dietblog.utils.PasswordBCryptHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AppUserServiceImpl appUserService;

    @Mock
    private PasswordBCryptHasher cryptHasher;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AppUser user;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        DataSource mockDataSource = mock(DataSource.class);
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        DatabaseConfig.setTestDataSource(mockDataSource); // Подмена на тестовую БД

        when(DatabaseConfig.createConnection()).thenReturn(mockConnection);

        // Настроим возвращаемое соединение

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }


    @Test
    void authenticate_ValidPassword_ReturnsTrue() {
        when(cryptHasher.verifyPassword("password", "hashed_password")).thenReturn(true);
        assertTrue(authenticationService.authenticate("password", user));
    }

    @Test
    void authenticate_InvalidPassword_ReturnsFalse() {
        when(cryptHasher.verifyPassword("wrong_password", "hashed_password")).thenReturn(false);
        assertFalse(authenticationService.authenticate("wrong_password", user));
    }

    @Test
    void isUserAuthenticateValid_UserExists_ReturnsTrue() {
        when(appUserService.findByEmail(user.getEmail())).thenReturn(user);
        assertTrue(authenticationService.isUserAuthenticateValid(user));
    }

    @Test
    void isUserAuthenticateValid_UserNotFound_ThrowsUnauthorizedAccessException() {
        when(appUserService.findByEmail(user.getEmail())).thenThrow(new UserNotFoundException("User not found"));
        assertThrows(UnauthorizedAccessException.class, () -> authenticationService.isUserAuthenticateValid(user));
    }

    @Test
    void convertPassword_ReturnsHashedPassword() {
        when(cryptHasher.hashPassword("password")).thenReturn("hashed_password");
        assertEquals("hashed_password", authenticationService.convertPassword("password"));
    }
}

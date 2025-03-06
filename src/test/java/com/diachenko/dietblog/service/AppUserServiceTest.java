package com.diachenko.dietblog.service;
/*  diet-blog
    28.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.appUser.AppUserDao;
import com.diachenko.dietblog.exception.DuplicateObjectException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserDao appUserDao;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser(1, "test@example.com", "pass", "test@mail.com", "user", LocalDateTime.of(2022, 10, 10, 10, 10, 10), "image.jpg");
    }

    @Test
    void test_save_NewUser_Success() {
        when(appUserDao.getAppUserByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(appUserDao.createAppUser(testUser)).thenReturn(testUser);

        AppUser savedUser = appUserService.save(testUser);
        assertEquals(testUser, savedUser);

        verify(appUserDao).createAppUser(testUser);
    }

    @Test
    void test_save_ExistingUser_ThrowsDuplicateException() {
        when(appUserDao.getAppUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(DuplicateObjectException.class, () -> appUserService.save(testUser));
    }

    @Test
    void test_findById_UserExists_ReturnsUser() {
        when(appUserDao.getAppUserById(1)).thenReturn(Optional.of(testUser));

        AppUser foundUser = appUserService.findById(1);
        assertEquals(testUser, foundUser);
    }

    @Test
    void test_findById_UserNotFound_ThrowsException() {
        when(appUserDao.getAppUserById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> appUserService.findById(1));
    }

    @Test
    void test_findByEmail_UserExists_ReturnsUser() {
        when(appUserDao.getAppUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        AppUser foundUser = appUserService.findByEmail(testUser.getEmail());
        assertEquals(testUser, foundUser);
    }

    @Test
    void test_findByEmail_UserNotFound_ThrowsException() {
        when(appUserDao.getAppUserByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> appUserService.findByEmail(testUser.getEmail()));
    }

    @Test
    void test_findAll_ReturnsListOfUsers() {
        List<AppUser> users = List.of(testUser, testUser);
        when(appUserDao.getAllAppUsers()).thenReturn(users);

        List<AppUser> result = appUserService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void test_deleteById_UserExists_ReturnsTrue() {
        when(appUserDao.deleteAppUser(1)).thenReturn(true);

        assertTrue(appUserService.deleteById(1));
    }

    @Test
    void test_deleteById_UserNotFound_ThrowsException() {
        when(appUserDao.deleteAppUser(1)).thenReturn(false);

        assertThrows(ServiceException.class, () -> appUserService.deleteById(1));
    }

    @Test
    void test_updateAppUserById_UserExists_ReturnsUpdatedUser() {
        when(appUserDao.getAppUserById(1)).thenReturn(Optional.of(testUser));
        AppUser updatedUser = appUserService.updateAppUserById(testUser);
        assertEquals(testUser, updatedUser);
    }
}


package org.example;

import org.example.service.AdminService;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    private AdminService adminService;
    private final String TEST_FILE = "admins_test.txt";

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
        adminService = new AdminService(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void testLoginSuccess() {
        assertTrue(adminService.login("anan", "1234"));
        assertTrue(adminService.isLoggedIn());
        assertNotNull(adminService.getCurrentAdmin());
        assertEquals("anan", adminService.getCurrentAdmin().getUsername());
    }

    @Test
    void testLoginFailure() {
        assertFalse(adminService.login("wrong", "user"));
        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
    }

    @Test
    void testLoadAdminsIOException() throws Exception {
        File f = new File(TEST_FILE);
        try (FileOutputStream out = new FileOutputStream(f)) {
            out.write(new byte[]{0,0,0,0,0});
        }
        AdminService bad = new AdminService(TEST_FILE);
        assertNotNull(bad.getAdmins());
    }

    @Test
    void testSaveAdminsIOException() {
        AdminService bad = new AdminService("/root/forbidden.txt");
        assertDoesNotThrow(() -> bad.addAdmin("x", "123"));
    }

    @Test
    void testLoadAdminsFileNotExist() {
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
        AdminService as = new AdminService(TEST_FILE);
        assertFalse(as.getAdmins().isEmpty());
    }

    @Test
    void testLoginWrongPassword() {
        assertFalse(adminService.login("anan", "wrong"));
        assertFalse(adminService.isLoggedIn());
    }

    @Test
    void testLoginWithNullValues() {
        assertFalse(adminService.login(null, null));
        assertFalse(adminService.isLoggedIn());
    }

    @Test
    void testLoginWithEmptyStrings() {
        assertFalse(adminService.login("", ""));
        assertFalse(adminService.isLoggedIn());
    }

    @Test
    void testMultipleFailedLogins() {
        adminService.login("wrong1", "123");
        adminService.login("wrong2", "abc");
        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
    }

    @Test
    void testLogoutAfterLogin() {
        adminService.login("anan", "1234");
        adminService.logout();
        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
    }

    @Test
    void testLoginSwitchBetweenAdmins() {
        assertTrue(adminService.login("anan", "1234"));
        assertEquals("anan", adminService.getCurrentAdmin().getUsername());
        adminService.logout();
        assertTrue(adminService.login("mohammad", "123"));
        assertEquals("mohammad", adminService.getCurrentAdmin().getUsername());
    }

    @Test
    void testAddNewAdminAndLogin() {
        adminService.addAdmin("newadmin", "pass");
        assertTrue(adminService.login("newadmin", "pass"));
    }

    @Test
    void testGetAdminsNotEmpty() {
        assertNotNull(adminService.getAdmins());
        assertFalse(adminService.getAdmins().isEmpty());
    }

    @Test
    void testAddAdminNullUsername() {
        int before = adminService.getAdmins().size();
        boolean added = adminService.addAdmin(null, "123");
        assertFalse(added);
        assertEquals(before, adminService.getAdmins().size());
    }

    @Test
    void testAddAdminEmptyUsername() {
        int before = adminService.getAdmins().size();
        boolean added = adminService.addAdmin("", "123");
        assertFalse(added);
        assertEquals(before, adminService.getAdmins().size());
    }

    @Test
    void testAddAdminNullPassword() {
        boolean added = adminService.addAdmin("user1", null);
        assertFalse(added);
        assertFalse(adminService.login("user1", "default123"));
    }

    @Test
    void testAddAdminEmptyPassword() {
        boolean added = adminService.addAdmin("user2", "");
        assertFalse(added);
        assertFalse(adminService.login("user2", "default123"));
    }

    @Test
    void testRemoveAdminNull() {
        int before = adminService.getAdmins().size();
        adminService.removeAdmin(null);
        assertEquals(before, adminService.getAdmins().size());
    }

    @Test
    void testRemoveAdminValid() {
        adminService.addAdmin("todelete", "000");
        assertTrue(adminService.getAdmins().stream().anyMatch(a -> a.getUsername().equals("todelete")));
        adminService.removeAdmin("todelete");
        assertFalse(adminService.getAdmins().stream().anyMatch(a -> a.getUsername().equals("todelete")));
    }

    @Test
    void testLoadAdminsFromEmptyFile() throws Exception {
        tearDown();
        File f = new File(TEST_FILE);
        f.createNewFile();
        AdminService as = new AdminService(TEST_FILE);
        assertFalse(as.getAdmins().isEmpty());
    }

    @Test
    void testSaveAdminsCreatesFile() {
        adminService.addAdmin("fileTest", "aaaa");
        File f = new File(TEST_FILE);
        assertTrue(f.exists());
        assertTrue(f.length() > 0);
    }
}

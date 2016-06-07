package TestDao;

import Core.User;
import Core.UserDao;
import javax.ejb.EJB;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserDao {

    @EJB
    private UserDao userDAO;

    private static User user;

    @BeforeClass
    public static void setUp() {
        user = new User("Username",
                "pass",
                "Email",
                "Pic path");
    }

    @Test
    public void testSearchByEmail() {
        User u = userDAO.searchByEmail("josemeilan242@gmail.com");
        assertThat(u.getUsername(), is(equalTo("jmmeilan")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSearchNullEmail() {
        userDAO.searchByEmail(null);
    }

    @Test
    public void testCheckDataUsername() {
        boolean check = userDAO.checkData("jmmeilan", "email que no existe");
        assertThat(check, is(equalTo(true)));
    }

    @Test
    public void testCheckDataEmail() {
        boolean check = userDAO.checkData("username no existente", "josemeilan242@gmail.com");
        assertThat(check, is(equalTo(true)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestCheckNullUser() {
        userDAO.checkData(null, "josemeilan242@gmail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestCheckNullEmail() {
        userDAO.checkData("jmmeilan", null);
    }

    @Test
    public void testAuthenticate() {
        boolean result = userDAO.authenticateUser(1201, "contraseña");
        assertThat(result, is(equalTo(true)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullAuthenticate() {
        userDAO.authenticateUser(1201, null);
    }

    @Test
    public void testUpdatePassword() {
        User u = userDAO.updatePassword(1201, "newPassword");
        //Comprobar con getpass no se puede por que viene hasheada??
        assertThat(u, is(not(equalTo(null))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullPassUpdate() {
        userDAO.updatePassword(1201, null);
    }

    @Test
    public void testUpdateEmail() {
        User u = userDAO.updateEmail(1201, "newEmail");
        assertThat(u.getEmail(), is(equalTo("newEmail")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullEmailUpdate() {
        userDAO.updateEmail(1201, null);
    }

    @Test
    public void testUpdatePicture() {
        User u = userDAO.updateEmail(1201, "newPath");
        assertThat(u.getPicturePath(), is(equalTo("newPath")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullPictureUpdate() {
        userDAO.updatePicture(1201, null);
    }

    @Test
    public void TestExists() {
        boolean result = userDAO.exists(1201);
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void TestRegister() {
        boolean result = userDAO.register(user);
        assertThat(result, is(equalTo(true)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullRegister() {
        userDAO.register(null);
    }

}

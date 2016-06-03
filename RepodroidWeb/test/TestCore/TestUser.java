package TestCore;

import Core.User;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TestUser {

    @Test
    public void TestGetIdUser() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        assertThat(0, is(equalTo(u.getNumUser())));
    }

    @Test
    public void TestSetIdUser() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setNumUser(1);
        assertThat(1, is(equalTo(u.getNumUser())));
    }

    @Test
    public void TestGetEmail() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        assertThat("email", is(equalTo(u.getEmail())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullEmail() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setEmail(null);
    }

    @Test
    public void TestSetEmail() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setEmail("nuevoEmail");
        assertThat("nuevoEmail", is(equalTo(u.getEmail())));
    }

    @Test
    public void TestGetPicturePath() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        assertThat("picturePath", is(equalTo(u.getPicturePath())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullPicturePath() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setPicturePath(null);
    }

    @Test
    public void TestSetPicturePath() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setPicturePath("nuevoPath");
        assertThat("nuevoPath", is(equalTo(u.getPicturePath())));
    }

    @Test
    public void TestGetUsername() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        assertThat("jmmeilan", is(equalTo(u.getUsername())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullUsername() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setUsername(null);
    }

    @Test
    public void TestSetUsername() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setUsername("username");
        assertThat("username", is(equalTo(u.getUsername())));
    }

    @Test
    public void TestGetPassword() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        assertThat("password", is(equalTo(u.getPassword())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullPassword() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setPassword(null);
    }

    @Test
    public void TestSetPassword() {
        User u = new User(0, "jmmeilan", "password", "email", "picturePath");
        u.setPassword("newPassword");
        assertThat("newPassword", is(equalTo(u.getPassword())));
    }

}

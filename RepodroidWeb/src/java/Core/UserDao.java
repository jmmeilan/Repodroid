package Core;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.jasypt.util.password.BasicPasswordEncryptor;

@Stateless
@LocalBean
public class UserDao extends GenericDao<User> {

    //Methods specific only to users
    public User searchByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("The email is null");
        }
        Query q = em.createQuery("SELECT u FROM User u "
                + "WHERE u.email = :email");
        q.setParameter("email", email);
        return getUniqueResult(q);
    }

    public Boolean checkData(String username, String email) {
        if (username == null || email == null) {
            throw new IllegalArgumentException("The arguments can't be null");
        }
        Query q = em.createQuery("SELECT u FROM User u "
                + "WHERE u.username = :username OR u.email = :email");
        q.setParameter("username", username);
        q.setParameter("email", email);
        List<User> results = q.getResultList();
        return results.size() > 0;
    }

    public boolean authenticateUser(int userId, String password) {
        if (password == null) {
            throw new IllegalArgumentException("The password is null");
        }
        User user;
        boolean toRet = false;

        user = searchById(userId);

        if (user != null) {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                toRet = true;
            }
        }

        return toRet;
    }

    public User updatePassword(int userId, String password) {
        if (password == null) {
            throw new IllegalArgumentException("The password is null");
        }
        User user = searchById(userId);

        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(password);

        user.setPassword(encryptedPassword);
        return update(user);
    }

    public User updateEmail(int userId, String email) {
        if (email == null) {
            throw new IllegalArgumentException("The email is null");
        }
        User user = searchById(userId);
        user.setEmail(email);
        return update(user);
    }

    public User updatePicture(int userId, String picture) {
        User user = searchById(userId);
        user.setPicturePath(picture);
        return update(user);
    }

    public boolean exists(int idUsuario) {
        return (searchById(idUsuario) != null);
    }

    public boolean register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("The user is null");
        }
        User usuarioRegistrado = user;
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(usuarioRegistrado.getPassword());
        usuarioRegistrado.setPassword(encryptedPassword);
        usuarioRegistrado = create(usuarioRegistrado);
        return usuarioRegistrado != null;
    }
}

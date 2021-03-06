package es.uvigo.esei.tfg.repodroid.web.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_USER")
    private int idUser;
    @Column(name = "USERNAME", unique = true)
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EMAIL", unique = true)
    private String email;
    @Column(name = "IMAGE_PATH")
    private String picturePath;

    public User() {
    }

    public User(String username, String password, String email, String pic) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.picturePath = pic;
    }

    public User(int id, String username, String password, String email, String pic) {
        this.idUser = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.picturePath = pic;
    }

    public int getNumUser() {
        return idUser;
    }

    public void setNumUser(int numUser) {
        this.idUser = numUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("The email is null");
        }
        this.email = email;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        if (picturePath == null) {
            throw new IllegalArgumentException("The path is null");
        }
        this.picturePath = picturePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("The username is null");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("The password is null");
        }
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.idUser);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.idUser, other.idUser)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.idUser + ", email=" + this.email + '}';
    }
}

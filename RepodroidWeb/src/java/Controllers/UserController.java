package Controllers;

import Core.User;
import Core.UserDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named(value = "UserController")
@SessionScoped
public class UserController implements Serializable {

    private boolean authenticated;
    private User currentUser;
    private String userName;
    private String password;
    private String email;
    private Part picture;

    @EJB
    private UserDao userDAO;

    public UserController() {
    }

    @PostConstruct
    public void inicializar() {
        this.authenticated = false;
        this.currentUser = null;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Part getPicture() {
        return picture;
    }

    public void setPicture(Part picture) {
        this.picture = picture;
    }

    public void doLogin() {
        if (this.email.equals("") || this.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You need to introduce your email and password in order to log in", ""));
        } else {
            User user = this.userDAO.searchByEmail(this.email);
            if (user != null) {
                if (authenticate(user.getNumUser(), this.password)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Wrong email or password", ""));
                }
            }
        }
        this.email = "";
        this.password = "";
        this.userName = "";
    }

    public String edit() {
        if (this.password == null) {
            this.password = this.currentUser.getPassword();
        }
        String picturePath;
        if (this.picture != null) {
            picturePath = "/home/jmmeilan/Descargas/Repodroid/RepodroidWeb/"
                    + "web/resources/webResources/img/"
                    + this.currentUser.getUsername() + "_"
                    + this.picture.getSubmittedFileName();
            try (InputStream input = this.picture.getInputStream()) {
                File img = new File(picturePath);
                if (!img.exists()) {
                    Files.copy(input, img.toPath());
                }
                this.picture.getInputStream().close();
                picturePath = "img/" + this.currentUser.getUsername() + "_"
                        + this.picture.getSubmittedFileName();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            picturePath = this.currentUser.getPicturePath();
        }
        this.userDAO.updatePassword(this.currentUser.getNumUser(), this.password);
        User edited = this.userDAO.updatePicture(this.currentUser.getNumUser(), picturePath);
        if (edited == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error while editing", ""));
        }
        this.currentUser = this.userDAO.searchByEmail(this.currentUser.getEmail());
        this.email = "";
        this.password = "";
        this.userName = "";
        return "/index?faces-redirect=true";
    }

    public String register() {
        boolean registered = false;
        if (this.email == null || this.password == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must introduce your email and password", ""));
        } else if (this.userDAO.checkData(this.userName, this.email)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "That email or username is not available", ""));
        } else {
            String picturePath;
            if (this.picture != null) {
                picturePath = "/home/jmmeilan/Descargas/Repodroid/RepodroidWeb/"
                        + "web/resources/webResources/img/"
                        + this.currentUser.getUsername() + "_"
                        + this.picture.getSubmittedFileName();
                try (InputStream input = this.picture.getInputStream()) {
                    File img = new File(picturePath);
                    if (!img.exists()) {
                        Files.copy(input, img.toPath());
                    }
                    this.picture.getInputStream().close();
                    picturePath = "img/" + this.currentUser.getUsername() + "_"
                            + this.picture.getSubmittedFileName();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                picturePath = "img/default.png";
            }

            User toRegister = new User(this.userName,
                    this.password,
                    this.email,
                    picturePath);
            registered = this.userDAO.register(toRegister);
            if (!registered) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error in the registration", ""));
            }
        }

        this.email = "";
        this.password = "";
        this.userName = "";

        if (registered) {
            return "/index?faces-redirect=true";
        } else {
            return "register.xhtml";
        }
    }

    private boolean authenticate(int userId,
            String unencryptedPass) {
        if (this.userDAO.authenticateUser(userId, unencryptedPass)) {
            this.authenticated = true;
            this.currentUser = this.userDAO.searchById(userId);
            return true;
        } else {
            this.authenticated = false;
            this.currentUser = null;
            return false;
        }
    }

    public String doLogOut() {
        this.authenticated = false;
        this.currentUser = null;
        this.email = "";
        this.password = "";
        this.userName = "";

        // End the session        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // Volver a la página principal
        return "/index?faces-redirect=true";
    }

}

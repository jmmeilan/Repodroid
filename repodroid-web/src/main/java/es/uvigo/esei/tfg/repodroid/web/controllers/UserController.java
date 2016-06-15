package es.uvigo.esei.tfg.repodroid.web.controllers;

import es.uvigo.esei.tfg.repodroid.web.entities.User;
import es.uvigo.esei.tfg.repodroid.web.entities.UserDao;
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

    public static String picturesDir = "/home/jmmeilan/NetBeansProjects/"
            + "repodroid/repodroid-web/src/main/webapp/resources/webResources/"
            + "img/";
            

    private boolean authenticated;
    private User currentUser;
    private String userName;
    private String password;
    private String email;
    private Part picture;
    //Para el formulario
    private String form_pass;
    private String form_email;

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

    public String getForm_pass() {
        return form_pass;
    }

    public void setForm_pass(String form_pass) {
        this.form_pass = form_pass;
    }

    public String getForm_email() {
        return form_email;
    }

    public void setForm_email(String form_email) {
        this.form_email = form_email;
    }

    public String doLogin() {
        if (this.email.equals("") || this.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You need to introduce your email and password in order to log in", ""));
        } else {
            User user = this.userDAO.searchByEmail(this.email);
            if (user != null) {
                if (authenticate(user.getNumUser(), this.password)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong email or password", ""));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong email or password", ""));
            }
        }
        return "/index.xhtml";
    }

    public String edit() {
        if (this.form_pass == null) {
            this.form_pass = this.currentUser.getPassword();
        }
        String picturePath;
        if (this.picture != null) {
            picturePath = picturesDir
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
        this.userDAO.updatePassword(this.currentUser.getNumUser(), this.form_pass);
        User edited = this.userDAO.updatePicture(this.currentUser.getNumUser(), picturePath);
        if (edited == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error while editing", ""));
        }
        this.currentUser = this.userDAO.searchByEmail(this.currentUser.getEmail());
        return "/index?faces-redirect=true";
    }

    public String register() {
        boolean registered = false;
        if (this.form_email == null || this.form_pass == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must introduce your email and password", ""));
        } else if (this.userDAO.checkData(this.userName, this.form_email)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "That email or username is not available", ""));
        } else {
            String picturePath;
            if (this.picture != null) {
                picturePath = picturesDir
                        + this.userName + "_"
                        + this.picture.getSubmittedFileName();
                try (InputStream input = this.picture.getInputStream()) {
                    File img = new File(picturePath);
                    if (!img.exists()) {
                        Files.copy(input, img.toPath());
                    }
                    this.picture.getInputStream().close();
                    picturePath = "img/" + this.userName + "_"
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
                    this.form_pass,
                    this.form_email,
                    picturePath);
            registered = this.userDAO.register(toRegister);
            if (!registered) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "There was an error in the registration", ""));
            }
        }

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

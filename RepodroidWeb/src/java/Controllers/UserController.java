package Controllers;

import Core.User;
import Core.UserDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean (name = "UserController")
@SessionScoped
public class UserController {
    
    private boolean authenticated;
    private User currentUser;
    private String userName;
    private String password;
    private String email;
    private Part picture;
    
    @EJB
    private UserDao userDAO;
    
    public UserController(){
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
    }
    
    
    //Returns true if the registration was done successfully ESTO SOBRA?
    public boolean register(){
        boolean registered = false;
        if (this.email == null || this.password == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must introduce your email and password", ""));
        } else {
            String picturePath;
            if(this.picture != null){
                picturePath = "/home/jmmeilan/Cuckoo/Pictures/"+this.picture.getSubmittedFileName();
                try(InputStream input = this.picture.getInputStream()){
                    Files.copy(input, new File ("/home/jmmeilan/Cuckoo/Pictures/"+picture.getSubmittedFileName()).toPath());
                    this.picture.getInputStream().close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex){
                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } else {
                picturePath = "/home/jmmeilan/Cuckoo/Pictures/default.png";
            }
            
            User toRegister = new User(this.userName, 
                                       this.password, 
                                       this.email,
                                       picturePath);
            registered = this.userDAO.register(toRegister);
            
        }
        if (!registered){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "There was an error in the registration", ""));
        }
        return registered;
    }
    
    private boolean authenticate(int userId, 
                         String unencryptedPass) {
        if (this.userDAO.authenticateUser(userId, unencryptedPass)){
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
        //usuarioDAO.actualizarUltimoAcceso(usuarioActual.getId()); Meto un último acceso¿?
        this.authenticated = false;
        this.currentUser = null;

        // End the session        
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // Volver a la página principal
        return "/index?faces-redirect=true"; //Como funciona esto¿?
    }
    
}

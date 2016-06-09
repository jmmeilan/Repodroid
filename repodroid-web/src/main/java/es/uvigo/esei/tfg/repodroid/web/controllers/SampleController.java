package es.uvigo.esei.tfg.repodroid.web.controllers;

import es.uvigo.esei.tfg.repodroid.web.entities.SampleReference;
import es.uvigo.esei.tfg.repodroid.web.entities.SampleReferenceDao;
import es.uvigo.esei.tfg.repodroid.web.entities.SampleRepresentation;
import es.uvigo.esei.tfg.repodroid.web.services.RepodroidService;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.SampleType;
import es.uvigo.esei.tfg.repodroid.core.store.SampleStore;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named(value = "SampleController")
//DEBE SER ESTO REQUESTSCOPED O CONVERSATIONSCOPED???? 
@SessionScoped
public class SampleController implements Serializable {

    private Part apkSample;
    private List<SampleReference> currentUserSamples;
    private SampleReference toShow;
    private String description;
    //LISTS OF PARAMETERS FOR THE QUERY, needs to be converted to a list (SEPARAR POR COMAS PARA METER EN LISTA)
    private List<SampleReference> queryResult;
    private String permissions;
    private String classes;
    private String outputConnections;
    private String antiViruses;

    //PARA MOSTRAR
    SampleRepresentation representation;

    @Inject
    private UserController userBean;

    @Inject
    private RepodroidService repodroidService;

    @EJB
    private SampleReferenceDao sampleDao;

    public Part getApkSample() {
        return apkSample;
    }

    public void setApkSample(Part apkSample) {
        this.apkSample = apkSample;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getOutputConnections() {
        return outputConnections;
    }

    public void setOutputConnections(String outputConnections) {
        this.outputConnections = outputConnections;
    }

    public String getAntiViruses() {
        return antiViruses;
    }

    public void setAntiViruses(String antiViruses) {
        this.antiViruses = antiViruses;
    }

    public SampleController() {
    }

    public List<SampleReference> getCurrentUserSamples() {
        return currentUserSamples;
    }

    public void setCurrentUserSamples(List<SampleReference> currentUserSamples) {
        this.currentUserSamples = currentUserSamples;
    }

    public SampleReference getToShow() {
        return toShow;
    }

    public void setToShow(SampleReference toShow) {
        this.toShow = toShow;
    }

    public SampleRepresentation getRepresentation() {
        return representation;
    }

    public void setRepresentation(SampleRepresentation representation) {
        this.representation = representation;
    }

    public List<SampleReference> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<SampleReference> queryResult) {
        this.queryResult = queryResult;
    }

    public String submitSample() {
        if (this.apkSample != null) {
            SampleReference sample = new SampleReference();
            String extension = this.apkSample.getSubmittedFileName()
                    .substring(apkSample.getSubmittedFileName().lastIndexOf(".") + 1, apkSample.getSubmittedFileName().length());
            if (extension.equals("apk")) {
                String samplePath = RepodroidService.sampleStorePath + "/SAMPLES/"
                        + this.userBean.getCurrentUser().getUsername() + "_"
                        + this.apkSample.getSubmittedFileName();
                try (InputStream input = this.apkSample.getInputStream()) {
                    File samp = new File(samplePath);
                    if (!samp.exists()) {
                        Files.copy(input, samp.toPath());
                    }
                    sample.setSamplePath(samplePath);
                    sample.setUser(this.userBean.getCurrentUser());
                    sample.setSampleName(this.apkSample.getSubmittedFileName());
                    sample.setSampleDescription(this.description);
                    sample.setSubmitDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    Sample sampleToAnalyze = new Sample(samplePath, SampleType.APK);
                    sampleToAnalyze.setId(SampleStore.computeNextSampleID());
                    sample.setStorerID(sampleToAnalyze.getId());
                    this.repodroidService.analyzeSample(sampleToAnalyze,
                            this.userBean.getCurrentUser().getEmail());
                    this.sampleDao.create(sample);
                    this.apkSample.getInputStream().close();
                    FacesContext.getCurrentInstance().addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                "Your sample is being analyzed, you will receive"
                                        + " a notification at the email " 
                                        + this.userBean.getCurrentUser().getEmail()
                                        +" when the analysis is done", ""));
                } catch (IOException ex) {
                    Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "The submitted file does not have the extension apk", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must submit an .apk file", ""));
        }
        return "/index.xhtml";
    }

    public String getSamplesFromUser() {
        this.currentUserSamples = this.sampleDao.getAllSamplesFromUser(this.userBean.getCurrentUser().getNumUser());
        return "viewUser.xhtml";
    }

    public String showSample() {
        this.representation = new SampleRepresentation();
        Sample samp = this.repodroidService.retrieveSample(this.toShow.getStorerID());
            this.representation = new SampleViewHelper(samp).extractRepresentation();
        return "sampleView.xhtml";
    }

    public String prepareSimilarityQuery() {
        if (this.userBean.isAuthenticated()) {
            this.currentUserSamples = this.sampleDao.getAllSamplesFromUser(this.userBean.getCurrentUser().getNumUser());
            return "similaritySearch.xhtml";
        }
        return "/index?faces-redirect=true";
    }

    public String doSimilarityQuery() {
        this.queryResult = new ArrayList();
        Sample samp = this.repodroidService.retrieveSample(this.toShow.getStorerID());
        List<Sample> result = this.repodroidService.similaritySearch(samp);
        for (Sample s : result) {
            this.queryResult.add(this.sampleDao.getReferenceFromStoreId(s.getId()));
        }
        return "searchResult.xhtml";
    }

    public String doParametrizedQuery() {
        this.queryResult = new ArrayList();
        List<Sample> result = this.repodroidService.parametrizedSearch(
                this.antiViruses,
                this.classes,
                this.permissions,
                this.outputConnections);
        for (Sample s : result) {
            this.queryResult.add(this.sampleDao.getReferenceFromStoreId(s.getId()));
        }
        this.antiViruses = "";
        this.classes = "";
        this.outputConnections = "";
        this.permissions = "";
        return "searchResult.xhtml";
    }
}

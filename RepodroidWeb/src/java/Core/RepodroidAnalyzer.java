package Core;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.SampleStore;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RepodroidAnalyzer implements Runnable {
    
    public static String email_sender = "repodroidnotifications@gmail.com";
    public static String username =  "repodroidnotifications";
    public static String password = "canesten";

    private SampleStore store;
    private CuckooAnalyzer analyzer;
    private Sample sampleToAnalyze;
    private String email;

    public RepodroidAnalyzer(SampleStore s, CuckooAnalyzer c, Sample sample, String email) {
        /* TODO: METER LOS PATHS ESTATICOS EN UN FICHERO APARTE PARA SU ACCESO Y MODIFICACION MAS FACIL*/
        this.analyzer = c;
        this.store = s;
        this.sampleToAnalyze = sample;
        this.email = email;
    }

    private void sendNotification() {
        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email_sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.email));
            message.setSubject("Analysis complete");
            message.setText("The analysis you submitted is ready."
                    + "You can check the report by accesing your account "
                    + "information in Repodroid.");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        List<Analysis> analyses = this.analyzer.analyzeSample(this.sampleToAnalyze);
        for (Analysis analysis : analyses) {
            this.sampleToAnalyze.addAnalysis(analysis.getAnalysisName(), analysis);
        }
        this.store.storeSample(this.sampleToAnalyze);
        //Codigo para enviar un email de notificacion
        sendNotification();
    }

}

/**
 * This class allows for the setup to send an email.
 */

package Email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EmailSetup {
    private static final String APPLICATION_NAME = "Soft Eng";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static com.google.api.services.gmail.Gmail service;

    /**
     * Set the Gmail Service object to allow for sending emails through gmail
     *
     * @param service
     */
    public static void setService(Gmail service) {
        EmailSetup.service = service;
    }

    /**
     * Get the Gmail Service object to allow for sending emails through gmail
     *
     * @return a Gmail Service object
     */
    public static Gmail getService() {
        return service;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = EmailSetup.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Create a MimeMessage object
     *
     * @return a MimeMessage object to allow for the sending of email from group alias
     * @throws IOException If the credentials.json file cannot be found.
     * @throws GeneralSecurityException
     * @throws MessagingException
     */
    public static MimeMessage messageSetup() throws IOException, GeneralSecurityException, MessagingException {


        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        setService(new com.google.api.services.gmail.Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build());

        Session session = Session.getDefaultInstance(System.getProperties(), null);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress("aquaamaroks@gmail.com"));

        return message;
    }

    /**
     * Create an email signature
     *
     * @return an email signature for Aqua Amaroks
     */
    public static String getSignature() {
        return "\n\nBest, \n\nAqua Amaroks \nTeam A, Software Engineering D19";
    }

    /**
     * Get current date and time
     *
     * @return String of current time
     */
    public static String getTime() {
        Date today = new Date();
        return today.toString();
    }
}

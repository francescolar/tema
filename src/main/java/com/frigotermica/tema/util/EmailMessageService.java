package com.frigotermica.tema.util;

import com.frigotermica.tema.service.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailMessageService {

    @Autowired
    private EmailSenderService senderService;

    public void sendWelcomeEmail(String userEmail, String userName, String password) throws MessagingException {
        String subject = "Benvenuto su TeMa";
        String message = "<h1 style=\"font-size:24px; font-weight:bold;\">Benvenuto nel portale per le manutenzioni Termo Manutenzioni</h1>\n" +
                "    <p>Questo è il contenuto della tua email, con formattazione standard.</p>\n" +
                "    <p>Puoi usare paragrafi, liste, e altri elementi HTML per strutturare il testo.</p>" +
                "<p>Il tuo username è: " + userName + "</p>" +
                "<p>La tua password è: " + password + "</p>" +
                "\n" +
                "<p><b>IMPORTANTE</b>" +
                "<p>Ricordati di cambiare la password il prima possibile. Puoi farlo" +
                "<p>accedendo al portale e selezionando \"Modifica profilo\" dalla barra" +
                "<p>delle funzioni.</p>";
        senderService.sendEmail(userEmail, subject, message);
    }

    public void sendResetEmail(String userEmail, String userName, String password) throws MessagingException {
        String subject = "IMPORTANTE: Reset password | TeMa";
        String message = "<h1 style=\"font-size:24px; font-weight:bold;\">Reset password portale per le manutenzioni Termo Manutenzioni</h1>\n" +
                "    <p>Questo è il contenuto della tua email, con formattazione standard.</p>\n" +
                "    <p>Puoi usare paragrafi, liste, e altri elementi HTML per strutturare il testo.</p>" +
                "<p>Il tuo username è: " + userName + "</p>" +
                "<p>La tua password è: " + password + "</p>" +
                "\n" +
                "<p><b>IMPORTANTE</b>" +
                "<p>Ricordati di cambiare la password il prima possibile. Puoi farlo" +
                "<p>accedendo al portale e selezionando \"Modifica profilo\" dalla barra" +
                "<p>delle funzioni.</p>";
        senderService.sendEmail(userEmail, subject, message);
    }
}
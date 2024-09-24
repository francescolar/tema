package com.frigotermica.tema.util;

import com.frigotermica.tema.service.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailMessageService {

    @Autowired
    private EmailSenderService senderService;

    public void sendWelcomeEmail(String userEmail, String userName, String name, String password) throws MessagingException {
        String subject = "Ciao, " + name + ". Benvenuto su TeMa | TeMa";
        String message = "<div style=\"border-radius: 15px; font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; overflow: hidden;\">" +
                "<div style=\"background-color:#000000; color:white; padding: 10px; border-top-left-radius: 15px; border-top-right-radius: 15px;\">" +
                "<h1 style=\"font-size:24px; font-weight:bold; margin: 0;\">Frigotermica S.R.L.</h1>" +
                "</div>" +
                "<div style=\"background-color:#fffde6; padding: 20px; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px;\">" +
                "<h2 style=\"font-size:20px; font-weight:bold;\">Benvenuto nel portale per le manutenzioni Termo Manutenzioni</h2>" +
                "<p>Sei pronto per entrare nel portale e registrare il tuo lavoro.</p>" +
                "<p>Questi sono i tuoi dati di accesso:</p>" +
                "<p><strong>Il tuo username:</strong> " + userName + "</p>" +
                "<p><strong>La tua password provvisoria:</strong> " + password + "</p><br>" +
                "<div style=\"text-align: center;\">" +
                "<p><strong>IMPORTANTE</strong></p>" +
                "<p>Ricordati di cambiare la password il prima possibile. Puoi farlo accedendo al portale e selezionando \"Modifica password\" dalla barra delle funzioni.</p>" +
                "</div>" +
                "</div>" +
                "</div>";

        senderService.sendEmail(userEmail, subject, message);
    }

    public void sendResetEmail(String userEmail, String userName, String name, String password) throws MessagingException {
        String subject = "IMPORTANTE: Reset password di " + name + " | TeMa";
        String message = "<div style=\"border-radius: 15px; font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; overflow: hidden;\">" +
                "<div style=\"background-color:#000000; color:white; padding: 10px; border-top-left-radius: 15px; border-top-right-radius: 15px;\">" +
                "<h1 style=\"font-size:24px; font-weight:bold; margin: 0;\">Frigotermica S.R.L.</h1>" +
                "</div>" +
                "<div style=\"background-color:#fffde6; padding: 20px; border-bottom-left-radius: 15px; border-bottom-right-radius: 15px;\">" +
                "<p>Ciao, " + name + ". Hai richiesto il cambio password per il tuo account.</p>" +
                "<p>Questi sono i tuoi nuovi dati di accesso:</p>" +
                "<p><strong>Il tuo username:</strong> " + userName + "</p>" +
                "<p><strong>La tua password:</strong> " + password + "</p><br>" +
                "<div style=\"text-align: center;\">" +
                "<p><strong>IMPORTANTE</strong></p>" +
                "<p>Ricordati di cambiare la password il prima possibile. Puoi farlo accedendo al portale e selezionando \"Modifica profilo\" dalla barra delle funzioni.</p>" +
                "</div>" +
                "</div>" +
                "</div>";

        senderService.sendEmail(userEmail, subject, message);
    }
}
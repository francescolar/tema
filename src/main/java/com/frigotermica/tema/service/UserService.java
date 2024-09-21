package com.frigotermica.tema.service;

import com.frigotermica.tema.models.UserModel;
import com.frigotermica.tema.util.DbUtilityUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    @Autowired
    private EmailSenderService senderService;

    public int registerUser(@Valid UserModel user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return 4;
        }
        try {
            boolean userExist = DbUtilityUser.checkUsername(user.getUsername());
            boolean emailExist = DbUtilityUser.checkEmail(user.getEmail());

            if (!userExist) {
                return 1;
            }

            if (!emailExist) {
                return 2;
            }

            String randomPassword = BasicPasswordGenerator.getRandomPassword();
            user.setPassword(randomPassword);
            String message = "<h1 style=\"font-size:24px; font-weight:bold;\">Benvenuto nel portale per le manutenzioni Termo Manutenzioni</h1>\n" +
                    "    <p>Questo è il contenuto della tua email, con formattazione standard.</p>\n" +
                    "    <p>Puoi usare paragrafi, liste, e altri elementi HTML per strutturare il testo.</p>" +
                    "<p>Il tuo username è: " + user.getUsername() + "</p>" +
                    "<p>La tua password è: " + randomPassword + "</p>" +
                    "\n" +
                    "<p><b>IMPORTANTE</b>" +
                    "<p>Ricordati di cambiare la password il prima possibile. Puoi farlo" +
                    "<p>accedendo al portale e selezionando \"Modifica profilo\" dalla barra" +
                    "<p>delle funzioni.</p>";
            System.out.println(randomPassword);
            senderService.sendEmail(user.getEmail(), "Benvenuto su TeMa", message);
            DbUtilityUser.insertPreparedStatement(user);
            return 3;

        } catch (Exception e) {
            e.printStackTrace();
            return 4;
        }
    }
}

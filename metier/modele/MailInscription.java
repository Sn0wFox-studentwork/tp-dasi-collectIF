package fr.insalyon.dasi.metier.modele;

import java.util.List;

public class MailInscription extends Mail {

    public MailInscription() {
    }

    public MailInscription(Adherent expediteur, Adherent dest, boolean succes) {
        super(expediteur,
            "Bienvenue chez Collect'IF",
            "Bonjour " + dest.getPrenom() + "\r\n" +
              (succes ? "Nous vous confirmons votre adhésion à l'association COLLECT'IF."
                      + "Votre numéro d'adhérent est : " + dest.getId() + "." :
            "Votre adhésion à l'association COLLECT'IF a malencontreusement échoué..."
            + "Merci de recommener ultérieurement."),
            dest);
    }
    
}

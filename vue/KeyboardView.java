package fr.insalyon.dasi.vue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.insalyon.dasi.dao.ActiviteDao;
import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Activite;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.metier.modele.Evenement;
import fr.insalyon.dasi.metier.modele.MailInscription;
import fr.insalyon.dasi.metier.services.ServiceMetier;
import fr.insalyon.dasi.metier.services.ServiceTechnique;
import fr.insalyon.dasi.util.Saisie;

public class KeyboardView {
	
	// MENU ABSOLU GENERAL
	private static final int QUITTER = 0;
	private static final int ACCUEIL = 1;
	private static final int CONNECTER = 2;
	private static final int DECONNECTER = 3;
	private static final int CREER_COMPTE = 4;
	private static final int CHOISIR = 5;
	private static final int HISTORIQUE = 6;
	private static final int DEMANDER_EVENEMENT = 7;
	private static final int GERER_EVENEMENTS = 8;
	
	private static Adherent adherentConnecte = null;
	
	public static void menu()
	{
		// Creation de l'entity manager
		JpaUtil.creerEntityManager();
	
		// Accueil du client
		int action = accueil();
		
		// Gestion des actions
		while(action != QUITTER)
		{
			switch(action)
			{
				case ACCUEIL:
					action = accueil();
					break;
				case CONNECTER:
					action = connecter();
					break;
				case DECONNECTER:
					action = deconnecter();
					break;
				case CREER_COMPTE:
					action = creerCompte();
					break;
				case CHOISIR:
					action = choisirQuoiFaire();
					break;
				case HISTORIQUE:
					action = voirHistorique();
					break;
				case DEMANDER_EVENEMENT:
					action = demanderEvenement();
					break;
				case GERER_EVENEMENTS:
					action = gererEvenements();
					break;
				default:
					mauvaiseAction();
					// Retour à l'action en cours
					break;
					
				// NB :	case QUITTER n'existe pas,
				//		puisqu'on sort de la boucle lorsque action = QUITTER
			}
		}
	}
	
	private static int accueil()
	{
		System.out.println("*************Bienvenue sur la page d'accueil de COLLECT'IF !*************");
		System.out.println("D'ici, vous pouvez :\n");
		System.out.println("[0] Quitter");
		System.out.println("[1] Tenter de vous connecter");
		System.out.println("[2] Créer un compte\n");
		int action = Saisie.lireInteger("Que voulez-vous faire ?");
		
		switch(action)
		{
			case 0:
				return QUITTER;
			case 1:
				return CONNECTER;
			case 2:
				return CREER_COMPTE;
			default:
				mauvaiseAction();
				return ACCUEIL;
		}
	}
	
	private static int connecter()
	{
		System.out.println("*************PAGE DE CONNEXION*************");
		System.out.println("D'ici, vous pouvez :\n");
		System.out.println("[1] Revenir à l'écran d'accueil");
		System.out.println("[email mdp] Connecter\n");
		String chaine = Saisie.lireChaine("Que voulez-vous faire ?");
		
		switch(chaine)
		{
			case "1":
				return ACCUEIL;
			default:
				String[] infos = chaine.split(" ");
				if(infos.length != 2)
				{
					mauvaiseAction();
					return CONNECTER;
				}
				Adherent adh = new Adherent();
				adh.setMail(infos[0]);
				adh.setMdp(infos[1]);
				adherentConnecte = ServiceMetier.authentificate(adh);
				if(adherentConnecte != null)
				{
					System.out.println(	"Vous êtes connecté en tant que " +
										adherentConnecte.getPrenom() + ".\n");
					return CHOISIR;
				}
				System.out.println("Impossible de vous identifier.\n");
				return CONNECTER;
		}
	}
	
	private static int deconnecter()
	{
		adherentConnecte = null;
		System.out.println("Vous avez été déconnecté.");
		return ACCUEIL;
	}
	
	private static int creerCompte()
	{
		System.out.println("*************PAGE D'INSCRIPTION*************");
		System.out.println("D'ici, vous pouvez :\n");
		System.out.println("[0] Revenir à l'écran d'accueil");
		System.out.println("[email mdp prenom nom] Creer un compte\n");
		String chaine = Saisie.lireChaine("Que voulez-vous faire ?");
		
		switch(chaine)
		{
			case "0":
				return ACCUEIL;
			default:
				String[] infos = chaine.split(" ");
				if(infos.length != 4)
				{
					mauvaiseAction();
					return CREER_COMPTE;
				}
				
				// Préparation de l'adhérent
				Adherent adh = new Adherent();
				adh.setMail(infos[0]);
				adh.setMdp(infos[1]);
				adh.setPrenom(infos[2]);
				adh.setNom(infos[3]);
				
				// Préparation du mail de confirmation/rejet
				MailInscription mail;
				ArrayList<Adherent> destinataire = new ArrayList<Adherent>();
				destinataire.add(adh);
				Adherent exp = new Adherent();
				exp.setMail("collectif@collectif.org");
				
				// Tentative d'inscription
				boolean inscriptionReussie = ServiceMetier.ajouterAdherent(adh);
				if(inscriptionReussie)
				{
					mail = new MailInscription(exp, adh, true);
					System.out.println("Votre compte a été créé.");					
				}
				else
				{
					mail = new MailInscription(exp, adh, false);
					System.out.println("La création de compte a échoué.");
				}
				System.out.println("Nous vous avons envoyé l'email suivant :");
				ServiceTechnique.sendMail(destinataire, mail);
				
				return inscriptionReussie ? CONNECTER : ACCUEIL;
		}
	}
	
	private static int choisirQuoiFaire()
	{
		System.out.println("*************PAGE DE CHOIX D'ACTION*************");
		System.out.println("D'ici, vous pouvez :\n");
		System.out.println("[0] Vous déconnecter");
		System.out.println("[1] Voir votre historique de demandes");
		System.out.println("[2] Faire une demande d'événement");
		if(adherentConnecte.isAdmin())
		{
			System.out.println("[3] Gérer les événements\n");
		}
		else
		{
			System.out.println("");
		}
		
		int action = Saisie.lireInteger("Que voulez-vous faire ?");
		
		switch(action)
		{
			case 0:
				return DECONNECTER;
			case 1:
				return HISTORIQUE;
			case 2:
				return DEMANDER_EVENEMENT;
			case 3:
				if(adherentConnecte.isAdmin())
				{
					return GERER_EVENEMENTS;
				}
				mauvaiseAction();
				return CHOISIR;
			default:
				mauvaiseAction();
				return CHOISIR;
		}
	}
	
	private static int voirHistorique()
	{
		System.out.println("*************PAGE D'HISTORIQUE*************");
		List<Evenement> le = ServiceMetier.getHistoriqueDemandes(adherentConnecte);
		for(Evenement ev: le)
		{
			System.out.println(ev);
		}
		System.out.println("\nCi-dessus se trouve l'historique de vos demandes.");
		System.out.println("Quand vous aurez fini de le consulter, vous pourrez :\n");
		System.out.println("[0] Revenir à la page de choix");
		System.out.println("[1] Rafraichir l'Historique\n");
		int action = Saisie.lireInteger("Que voulez-vous faire ?");
		
		switch(action)
		{
			case 0:
				return CHOISIR;
			case 1:
				return HISTORIQUE;
			default:
				mauvaiseAction();
				return CHOISIR;
		}
	}
	
	private static int demanderEvenement()
	{
		System.out.println("*************PAGE DE DEMANDE D'EVENEMENT*************");
		ActiviteDao actDao = new ActiviteDao();
		try {
			List<Activite> la = actDao.findAll();
			for(Activite a: la)
			{
				System.out.println("ID: " + a.getId() + " - " + a.getDenomination());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("\nCi-dessus se trouvent toutes les activitées disponibles.");
		System.out.println("Quand vous aurez fini de le consulter, vous pourrez :\n");
		System.out.println("[0] Revenir à la page de choix");
		System.out.println("[idActivité] Créer un événement associé à l'activité dont vous avez tapé l'ID\n");
		int action = Saisie.lireInteger("Que voulez-vous faire ?");
		
		switch(action)
		{
			case 0:
				return CHOISIR;
			default:
				Activite act;
				try {
					act = actDao.findById(action);
					if(act == null)
					{
						mauvaiseAction();
						return DEMANDER_EVENEMENT;
					}
					String confirmation = "";
					System.out.println("Vous allez créer une demande d'événement et revenir à l'écran de choix.");
					do
					{
						confirmation = Saisie.lireChaine("Voulez-vous continuer ?(y/n)");
						if(confirmation.toLowerCase().equals("n"))
						{
							System.out.println("Annulation...");
							return CHOISIR;
						}
					} while(!confirmation.toLowerCase().equals("y"));
					
					Evenement event = new Evenement();
					event.setActivite(act);
					ServiceMetier.createOrFillEvent(event, adherentConnecte);
				} catch (Throwable ex) {
					Logger.getLogger(KeyboardView.class.getName()).log(Level.SEVERE, null, ex);
				}
				return CHOISIR;
		}
	}
	
	private static int gererEvenements()
	{
		return ACCUEIL;
	}
	
	private static void mauvaiseAction()
	{
		System.out.println("Désolé, vous n'avez pas saisie une action valable.");
	}
	
	
}

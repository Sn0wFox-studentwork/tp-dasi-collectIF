package fr.insalyon.dasi.vue;

import fr.insalyon.dasi.dao.JpaUtil;
import fr.insalyon.dasi.metier.modele.Adherent;
import fr.insalyon.dasi.util.Saisie;

public class KeyboardView {
	
	private static Adherent adherentConnecte = null;
	
	public static void menu()
	{
		// Creation de l'entity manager
		JpaUtil.creerEntityManager();
	
		// Accueil du client et gestion de ces actions
		accueil();
	}
	
	private static void accueil()
	{
		System.out.println("*************Bienvenue sur la page d'accueil de COLLECT'IF !*************");
		System.out.println("D'ici, vous pouvez :");
		System.out.println("[0] Quitter");
		System.out.println("[1] Tenter de vous connecter");
		System.out.println("[2] Créer un compte\n");		
		int action = Saisie.lireInteger("Que voulez-vous faire ?");
		
		switch(action)
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			default:
				mauvaiseAction();
				accueil();
				break;
		}
	}
	
	private static void connecter()
	{
		
	}
	
	private static void deconnecter()
	{
		
	}
	
	private static void creerCompte()
	{
		
	}
	
	private static void choisirQuoiFaire()
	{
		
	}
	
	private static void voirHistorique()
	{
		
	}
	
	private static void demanderEvenement()
	{
		
	}
	
	private static void gererEvenements()
	{
		
	}
	
	private static void mauvaiseAction()
	{
		System.out.println("Désolé, vous n'avez pas saisie une action valable.");
	}
	
	
}

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
Le RobotBall est un jeu invent� mais inspir� d'une application sur un t�l�phone
(Snake Vs Blocks). 

Il s'agit de d�placer un robot pour qu'il accumule des balles d'�nergie pour les 
distribuer � des points de ravitaillement.  Le robot doit toujours en avoir sur
lui sinon il n'a plus d'�nergie et s'autod�truit.

Le d�tail du d�roulement du jeu est fourni dans l'�nonc�.


Finalement, il est possible de rejouer une m�me partie ou de jouer une nouvelle 
partie.

Des statistiques sur les pointages et le nombre de balles sont donn�es 
� la fin de la partie.

Dans le cadre du cours inf111
�cole de technologie sup�rieure

 @author Pierre B�lisle 
 @version (copyright 2020)
 */

public class DemarrerRobotBall{

	/*
	 * Strat�gie globale : On utilise principalement le module UtilitaireJeu qui
	 * contient les sous-programmes de d�placement et d'affichage de la grille 
	 * les SP des diff�rents modules sp�cifiques pour obtenir une grille que
	 * l'on se sert pour remplir  la fen�tre gui. 
	 * 
	 * C'est ici qu'on g�re la boucle principale qui se termine si l'utilisateur
	 * quitte, s'il r�ussit ou �choue le niveau.  Dans cette boucle, on affiche
	 *  es obstacles et le robot. On obtient si l'utilisateur a press� sur une
	 *  fl�che et selon,
	 *   
	 * on modifie la trajectoire du robot, on �value s'il touche un obstacle
	 * et on ajuste les balles ou les points.  	
	 * 
	 * Le reste de la strat�gie est d�crite en commentaire-ligne au fur et �
	 *  mesure.
	 */

	public static void main(String[] args) throws IOException {


		// Obtenir la dimension de l'�cran en temps r�el.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Cr�ation de l'interface graphique qui permet de jouer.
		// Il restera � la remplir.
		// Le nombre de lignes et de colonnes du gui sont calcul�s � 
		// partir de la dimension de l'�cran.
		GrilleGui gui = new GrilleGui(dim, 
				Constantes.COULEUR_TEXTE_DEFAUT, 
				Constantes.COULEUR_FOND_DEFAUT, 
				Constantes.OPTIONS);

		// On obtient la dimensions r�elles de la grille.
		int nbLignes = gui.getNbLignes();
		int nbColonnes = gui.getNbColonnes();
		
		// �vite le recalcul pour l'appel des SP.
		int ligneCentre = nbLignes/2;
		int colonneCentre = nbColonnes /2;
		
		// Sert pour ne pas donner le message de fin de partie � chaque tour 
		// de boucle en attendant que l'utilisateur clique sur un bouton de menu.
		boolean dejaAvise;
		
		// On obtient une premi�re grille avec obstacles.
		int[][] grille = new int[nbLignes][nbColonnes];

		// Sert � retenir la grille de d�part pour la restauration.
		int[][] copieGrille = new int[nbLignes][nbColonnes];
		
		// En suivant les r�gles de remplisssage  d�crites dans l'�nonc�.
	    UtilitaireJeu.obtenirNouvelleGrille(grille,
	    		                            copieGrille, 
	    		                            nbLignes, 
	    		                            nbColonnes);

		// Les stats � maintenir.
		Statistiques stats = new Statistiques();
		
		// Le robot.
		Robot robot = new Robot();

        // Un robot avec quelques balles au d�part.
		init(robot, Constantes.NB_BALLES_DEPART, ligneCentre, colonneCentre);
		
		dejaAvise = false;

		//Boucle infinie qui se termine si l'utilisateur gagne ou s'il quitte 
		//en cliquant sur X.  C'est un tour de la simulation par tour de boucle.
		while(true){
		
			// Affiche les obstacles, le robot et les balles dans le gui.
			UtilitaireJeu.afficherGrille(gui, grille);
			UtilitaireJeu.afficherRobot(gui, robot);

			//L'utilisateur a cliqu� sur un des boutons d'options
			if(gui.optionMenuEstCliquee()){

				gererMenu(gui, robot, grille, copieGrille, stats);			
				dejaAvise = false;
			}

			else{

				// Si le robot a encore des balles de vies.
				if(robot.nbBalles > 0 && 
					UtilitaireJeu.ilResteDesCentres(grille)){
					
					UtilitaireJeu.effectuerTour(gui,	grille, robot, stats);					
				}
				
				//S'il n'y a plus de balles, le robot meurt et la partie est
				//termin�e.
				else{
					
				    // Comme c'est en boucle jusqu'� ce qu'un bouton du menu 
					// soit touch�e, On avise pas chaque fois.
					if(!dejaAvise){
						
						dejaAvise = true;
						JOptionPane.showMessageDialog(null,  "Partie termin�e");

						// Affichage des statistiques.
						JOptionPane.showMessageDialog(null, "Pointage : "+
								String.valueOf(stats.pointage) +"\n" +
								"Nombre de balles maximum obtenu : " +
								String.valueOf(stats.maxBalles) +"\n" +
								"Record :" +
								String.valueOf(stats.hautPointage) +"\n");
					}
				}
			}
		}
	}
	
	/*
	 * Proc�dure priv�e qui g�re les actions pour le clique d'un bouton de menu. 
	 */
	private static void gererMenu(GrilleGui gui, 
			Robot robot, 
			int[][] grille, 
			int[][] copieGrille,
			Statistiques stats) {

		// On r�cup�re la cha�ne de l'option choisie.
		String reponse = gui.getOptionMenuClique();
		
		int nbLignes = gui.getNbLignes();
		int nbColonnes = gui.getNbColonnes();
		
		// �vite le recalcul.
		int ligneCentre = nbLignes/2;
		int colonneCentre = nbColonnes/2;

		// Une nouvelle partie
		 if(reponse.equals(
				Constantes.OPTIONS[Constantes.NOUVELLE_PARTIE])){


			UtilitaireJeu.viderGrille(grille);
			UtilitaireJeu.obtenirNouvelleGrille(grille, 
					                            copieGrille, 
					                            nbLignes,
					                            nbColonnes);
		}
		
		// RECOMMENCER
		else{
				UtilitaireJeu.restaurerGrilleDepart(grille, copieGrille);
		}
		
		 // Replace le robot.
		init(robot, Constantes.NB_BALLES_DEPART, ligneCentre,  colonneCentre);
		
			// On ne touche pas aux autres champs pour retenir leur valeur.
   	    stats.pointage = 0;
	}
	
    /**
     * Initialise le tableau de balles du robot avec 
     * son nombre de balles au d�part.
     * 
     */
    public static void init(Robot robot, 
    		                int nbBallesDepart, 
    		                int ligne, 
    		                int colonne) {
    	
    	robot.nbBalles = 0;
    	
    	for(int i = 0 ; i < nbBallesDepart;i++){
    		robot.tabBalles[robot.nbBalles] =  new Coordonnee();
    		robot.tabBalles[robot.nbBalles] .ligne = ligne++;
    		robot.tabBalles[robot.nbBalles] .colonne  = colonne;
    	    robot.nbBalles++;
    	}
	}
}
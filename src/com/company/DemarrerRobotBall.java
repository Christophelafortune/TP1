import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
Le RobotBall est un jeu inventé mais inspiré d'une application sur un téléphone
(Snake Vs Blocks). 

Il s'agit de déplacer un robot pour qu'il accumule des balles d'énergie pour les 
distribuer à des points de ravitaillement.  Le robot doit toujours en avoir sur
lui sinon il n'a plus d'énergie et s'autodétruit.

Le détail du déroulement du jeu est fourni dans l'énoncé.


Finalement, il est possible de rejouer une même partie ou de jouer une nouvelle 
partie.

Des statistiques sur les pointages et le nombre de balles sont données 
à la fin de la partie.

Dans le cadre du cours inf111
École de technologie supérieure

 @author Pierre Bélisle 
 @version (copyright 2020)
 */

public class DemarrerRobotBall{

	/*
	 * Stratégie globale : On utilise principalement le module UtilitaireJeu qui
	 * contient les sous-programmes de déplacement et d'affichage de la grille 
	 * les SP des différents modules spécifiques pour obtenir une grille que
	 * l'on se sert pour remplir  la fenêtre gui. 
	 * 
	 * C'est ici qu'on gère la boucle principale qui se termine si l'utilisateur
	 * quitte, s'il réussit ou échoue le niveau.  Dans cette boucle, on affiche
	 *  es obstacles et le robot. On obtient si l'utilisateur a pressé sur une
	 *  flèche et selon,
	 *   
	 * on modifie la trajectoire du robot, on évalue s'il touche un obstacle
	 * et on ajuste les balles ou les points.  	
	 * 
	 * Le reste de la stratégie est décrite en commentaire-ligne au fur et à
	 *  mesure.
	 */

	public static void main(String[] args) throws IOException {


		// Obtenir la dimension de l'écran en temps réel.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Création de l'interface graphique qui permet de jouer.
		// Il restera à la remplir.
		// Le nombre de lignes et de colonnes du gui sont calculés à 
		// partir de la dimension de l'écran.
		GrilleGui gui = new GrilleGui(dim, 
				Constantes.COULEUR_TEXTE_DEFAUT, 
				Constantes.COULEUR_FOND_DEFAUT, 
				Constantes.OPTIONS);

		// On obtient la dimensions réelles de la grille.
		int nbLignes = gui.getNbLignes();
		int nbColonnes = gui.getNbColonnes();
		
		// Évite le recalcul pour l'appel des SP.
		int ligneCentre = nbLignes/2;
		int colonneCentre = nbColonnes /2;
		
		// Sert pour ne pas donner le message de fin de partie à chaque tour 
		// de boucle en attendant que l'utilisateur clique sur un bouton de menu.
		boolean dejaAvise;
		
		// On obtient une première grille avec obstacles.
		int[][] grille = new int[nbLignes][nbColonnes];

		// Sert à retenir la grille de départ pour la restauration.
		int[][] copieGrille = new int[nbLignes][nbColonnes];
		
		// En suivant les règles de remplisssage  décrites dans l'énoncé.
	    UtilitaireJeu.obtenirNouvelleGrille(grille,
	    		                            copieGrille, 
	    		                            nbLignes, 
	    		                            nbColonnes);

		// Les stats à maintenir.
		Statistiques stats = new Statistiques();
		
		// Le robot.
		Robot robot = new Robot();

        // Un robot avec quelques balles au départ.
		init(robot, Constantes.NB_BALLES_DEPART, ligneCentre, colonneCentre);
		
		dejaAvise = false;

		//Boucle infinie qui se termine si l'utilisateur gagne ou s'il quitte 
		//en cliquant sur X.  C'est un tour de la simulation par tour de boucle.
		while(true){
		
			// Affiche les obstacles, le robot et les balles dans le gui.
			UtilitaireJeu.afficherGrille(gui, grille);
			UtilitaireJeu.afficherRobot(gui, robot);

			//L'utilisateur a cliqué sur un des boutons d'options
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
				//terminée.
				else{
					
				    // Comme c'est en boucle jusqu'à ce qu'un bouton du menu 
					// soit touchée, On avise pas chaque fois.
					if(!dejaAvise){
						
						dejaAvise = true;
						JOptionPane.showMessageDialog(null,  "Partie terminée");

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
	 * Procédure privée qui gère les actions pour le clique d'un bouton de menu. 
	 */
	private static void gererMenu(GrilleGui gui, 
			Robot robot, 
			int[][] grille, 
			int[][] copieGrille,
			Statistiques stats) {

		// On récupère la chaîne de l'option choisie.
		String reponse = gui.getOptionMenuClique();
		
		int nbLignes = gui.getNbLignes();
		int nbColonnes = gui.getNbColonnes();
		
		// Évite le recalcul.
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
     * son nombre de balles au départ.
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
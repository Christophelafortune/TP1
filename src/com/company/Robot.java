/**
 * Cet enregistrement (champs publiques)  regroupe la position du robot,  
 * sa chaîne de balles et son nombre d'éléments significatifs. 
 * 
 * @author Pierre Bélisle 
 * @version (copyright A2020)
*/
public class Robot {

    // La première case est la position du robot 
    // (comme si c'était la position de la première balle).
    // Les autres ses contiennent la position des autres balles.
	
    // Les balles d'énergie du robot.
    public Coordonnee[] tabBalles = new Coordonnee[Constantes.MAX_BALLES];
    
    // Inclus le robot.
    public int nbBalles;

}

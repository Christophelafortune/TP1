/**
 * Cet enregistrement (champs publiques)  regroupe la position du robot,  
 * sa cha�ne de balles et son nombre d'�l�ments significatifs. 
 * 
 * @author Pierre B�lisle 
 * @version (copyright A2020)
*/
public class Robot {

    // La premi�re case est la position du robot 
    // (comme si c'�tait la position de la premi�re balle).
    // Les autres ses contiennent la position des autres balles.
	
    // Les balles d'�nergie du robot.
    public Coordonnee[] tabBalles = new Coordonnee[Constantes.MAX_BALLES];
    
    // Inclus le robot.
    public int nbBalles;

}

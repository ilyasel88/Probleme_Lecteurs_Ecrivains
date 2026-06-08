import java.util.Random;

public class Lecteur implements Runnable {
    private int id;
    private RessourcePartagee ressource;
    private Simulateur simulateur;
    private InterfaceGraphique gui;
    private static int compteur = 0;
    
    public Lecteur(int id, RessourcePartagee ressource, Simulateur simulateur, InterfaceGraphique gui) {
        this.id = id;
        this.ressource = ressource;
        this.simulateur = simulateur;
        this.gui = gui;
    }
    public static synchronized void reinitialiserCompteur() {
    compteur = 0;
}
    
    public static synchronized int getCompteurLectures() {
        return compteur;
    }
    
    private static synchronized void incrementerCompteur() {
        compteur++;
    }
    
    public void run() {
        Random rand = new Random();
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1500 + rand.nextInt(2000));
            } catch (Exception e) {
                break;
            }
            
            ressource.lire(id);
            incrementerCompteur();
            gui.majLectures(compteur);
        }
        
        gui.ajouterMessage("Lecteur " + id + " arrete");
    }
}

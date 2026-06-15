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
        
        // Delai d'arrivee aleatoire (0 a 2000ms)
        try {
            Thread.sleep(rand.nextInt(2000));
        } catch (Exception e) {}
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            ressource.lire(id);
            incrementerCompteur();
            gui.majLectures(compteur);
            
            // Pause aleatoire avant la prochaine demande (200 a 1500ms)
            try {
                Thread.sleep(200 + rand.nextInt(1300));
            } catch (Exception e) {
                break;
            }
        }
        
        gui.ajouterMessage("Lecteur " + id + " arrete");
    }
}

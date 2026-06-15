import java.util.Random;

public class Ecrivain implements Runnable {
    private int id;
    private RessourcePartagee ressource;
    private Simulateur simulateur;
    private InterfaceGraphique gui;
    private static int compteurEcritures = 0;
    private static int compteurModifs = 0;
    
    public Ecrivain(int id, RessourcePartagee ressource, Simulateur simulateur, InterfaceGraphique gui) {
        this.id = id;
        this.ressource = ressource;
        this.simulateur = simulateur;
        this.gui = gui;
    }
    
    public static synchronized void reinitialiserCompteur() {
        compteurEcritures = 0;
        compteurModifs = 0;
    }
    
    public static synchronized int getCompteurEcritures() {
        return compteurEcritures;
    }
    
    private static synchronized String getNouvelleValeur(int id) {
        compteurModifs++;
        return "Donnee-" + compteurModifs + "-par-" + id;
    }
    
    private static synchronized void incrementerCompteurEcritures() {
        compteurEcritures++;
    }
    
    public void run() {
        Random rand = new Random();
        
        // Delai d'arrivee aleatoire (0 a 2000ms)
        try {
            Thread.sleep(rand.nextInt(2000));
        } catch (Exception e) {}
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            String valeur = getNouvelleValeur(id);
            ressource.ecrire(id, valeur);
            incrementerCompteurEcritures();
            gui.majEcritures(compteurEcritures);
            
            // Pause aleatoire avant la prochaine demande (500 a 2000ms)
            try {
                Thread.sleep(500 + rand.nextInt(1500));
            } catch (Exception e) {
                break;
            }
        }
        
        gui.ajouterMessage("Ecrivain " + id + " arrete");
    }
}

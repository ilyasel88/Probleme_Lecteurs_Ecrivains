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
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(2000 + rand.nextInt(2500));
            } catch (Exception e) {
                break;
            }
            
            String valeur = getNouvelleValeur(id);
            ressource.ecrire(id, valeur);
            incrementerCompteurEcritures();
            gui.majEcritures(compteurEcritures);
        }
        
        gui.ajouterMessage("Ecrivain " + id + " arrete");
    }
}
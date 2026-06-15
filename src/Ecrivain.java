import java.util.Random;

public class Ecrivain implements Runnable {
    private int id;
    private RessourcePartagee ressource;
    private Simulateur simulateur;
    private InterfaceGraphique gui;
    private static int compteurEcritures = 0;
    private static int compteurModifs = 0;
    private Random rand = new Random();
    
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
        // Pause aleatoire avant la premiere demande
        try { Thread.sleep(rand.nextInt(500)); } catch (Exception e) {}
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            // 1. Demander l'acces (entrer dans la file)
            int position = ressource.demanderEcriture(id);
            
            // 2. Creer la nouvelle valeur
            String valeur = getNouvelleValeur(id);
            
            // 3. Ecrire (attend son tour si necessaire)
            ressource.ecrire(id, position, valeur);
            
            incrementerCompteurEcritures();
            gui.majEcritures(compteurEcritures);
            
            // 4. Pause avant la prochaine demande
            try { Thread.sleep(1000 + rand.nextInt(1500)); } catch (Exception e) { break; }
        }
        
        gui.ajouterMessage("Ecrivain " + id + " arrete");
    }
}

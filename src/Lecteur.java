import java.util.Random;

public class Lecteur implements Runnable {
    private int id;
    private RessourcePartagee ressource;
    private Simulateur simulateur;
    private InterfaceGraphique gui;
    private static int compteur = 0;
    private Random rand = new Random();
    
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
        // Pause aleatoire avant la premiere demande
        try { Thread.sleep(rand.nextInt(500)); } catch (Exception e) {}
        
        while (!simulateur.estFini() && !Thread.currentThread().isInterrupted()) {
            // 1. Demander l'acces (entrer dans la file)
            int position = ressource.demanderLecture(id);
            
            // 2. Lire (attend son tour si necessaire)
            ressource.lire(id, position);
            
            incrementerCompteur();
            gui.majLectures(compteur);
            
            // 3. Pause avant la prochaine demande
            try { Thread.sleep(500 + rand.nextInt(1000)); } catch (Exception e) { break; }
        }
        
        gui.ajouterMessage("Lecteur " + id + " arrete");
    }
}

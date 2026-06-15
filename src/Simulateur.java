import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Simulateur {
    
    private int nbLecteurs;
    private int nbEcrivains;
    private int dureeSimulationMs;
    
    private RessourcePartagee ressource;
    private List<Thread> threads;
    private boolean fini;
    private InterfaceGraphique gui;
    private Random rand = new Random();
    
    public Simulateur(InterfaceGraphique gui, int nbLecteurs, int nbEcrivains, int dureeSimulationMs) {
        this.gui = gui;
        this.nbLecteurs = nbLecteurs;
        this.nbEcrivains = nbEcrivains;
        this.dureeSimulationMs = dureeSimulationMs;
        this.ressource = new RessourcePartagee(gui);
        this.threads = new ArrayList<>();
        this.fini = false;
    }
    
    public void demarrer() {
        // Liste pour stocker les threads avant de les demarrer
        List<Runnable> taches = new ArrayList<>();
        
        // Ajouter tous les lecteurs
        for (int i = 1; i <= nbLecteurs; i++) {
            taches.add(new Lecteur(i, ressource, this, gui));
            gui.ajouterMessage("[CREATION] Lecteur " + i);
        }
        
        // Ajouter tous les ecrivains
        for (int i = 1; i <= nbEcrivains; i++) {
            taches.add(new Ecrivain(i, ressource, this, gui));
            gui.ajouterMessage("[CREATION] Ecrivain " + i);
        }
        
        // Melanger l'ordre de demarrage (aleatoire)
        Collections.shuffle(taches, rand);
        
        // Separateur
        gui.ajouterMessage("");
        gui.ajouterMessage("----------------------------------------");
        gui.ajouterMessage("");
        gui.ajouterMessage(">>> SIMULATION EN COURS (" + (dureeSimulationMs / 1000) + " secondes) <<<");
        gui.ajouterMessage("");
        
        // Demarrer les threads dans l'ordre melange
        for (Runnable tache : taches) {
            Thread t = new Thread(tache);
            threads.add(t);
            t.start();
            
            // Petite pause aleatoire entre chaque demarrage (0 a 100ms)
            try { Thread.sleep(rand.nextInt(100)); } catch (Exception e) {}
        }
        
        try { Thread.sleep(dureeSimulationMs); } catch (Exception e) {}
        
        arreter();
    }
    
    public void arreter() {
        gui.ajouterMessage("");
        gui.ajouterMessage(">>> ARRET DE LA SIMULATION <<<");
        
        fini = true;
        
        for (Thread t : threads) {
            t.interrupt();
        }
        
        for (Thread t : threads) {
            try { t.join(1000); } catch (Exception e) {}
        }

        int nbLectures  = Lecteur.getCompteurLectures();
        int nbEcritures = Ecrivain.getCompteurEcritures();
        String valeurFinale = ressource.getDonnee();

        gui.ajouterMessage("");
        gui.ajouterMessage("=== STATISTIQUES FINALES ===");
        gui.ajouterMessage("Lectures  : " + nbLectures);
        gui.ajouterMessage("Ecritures : " + nbEcritures);
        gui.ajouterMessage("Valeur finale : " + valeurFinale);
        
        gui.majLectures(nbLectures);
        gui.majEcritures(nbEcritures);

        Log.fermerAvecStatistiques(nbLectures, nbEcritures, valeurFinale);
    }
    
    public boolean estFini() {
        return fini;
    }
}

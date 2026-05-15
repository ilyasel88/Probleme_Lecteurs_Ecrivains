import java.util.ArrayList;
import java.util.List;

public class Simulateur {
    
    private int nbLecteurs;
    private int nbEcrivains;
    private int dureeSimulationMs;
    
    private RessourcePartagee ressource;
    private List<Thread> threads;
    private boolean fini;
    private InterfaceGraphique gui;
    
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
        gui.ajouterMessage("");
        gui.ajouterMessage(">>> SIMULATION EN COURS (" + (dureeSimulationMs / 1000) + " secondes) <<<");
        gui.ajouterMessage("");
        
        try { Thread.sleep(100); } catch (Exception e) {}
        
        for (int i = 1; i <= nbLecteurs; i++) {
            gui.ajouterMessage("[DEMARRE] Lecteur " + i);
            Thread lecteur = new Thread(new Lecteur(i, ressource, this, gui));
            lecteur.start();
            threads.add(lecteur);
            try { Thread.sleep(200); } catch (Exception e) {}
        }
        
        for (int i = 1; i <= nbEcrivains; i++) {
            gui.ajouterMessage("[DEMARRE] Ecrivain " + i);
            Thread ecrivain = new Thread(new Ecrivain(i, ressource, this, gui));
            ecrivain.start();
            threads.add(ecrivain);
            try { Thread.sleep(200); } catch (Exception e) {}
        }
        
        gui.ajouterMessage("");
        gui.ajouterMessage("----------------------------------------");
        gui.ajouterMessage("");
        
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

        // --- LOG : ecriture des statistiques finales et fermeture du fichier ---
        Log.fermerAvecStatistiques(nbLectures, nbEcritures, valeurFinale);
    }
    
    public boolean estFini() {
        return fini;
    }
}

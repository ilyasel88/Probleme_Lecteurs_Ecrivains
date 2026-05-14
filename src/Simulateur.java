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
        for (int i = 1; i <= nbLecteurs; i++) {
            Thread lecteur = new Thread(new Lecteur(i, ressource, this, gui));
            lecteur.start();
            threads.add(lecteur);
            gui.ajouterMessage("Lecteur " + i + " demarre");
            try { Thread.sleep(500); } catch (Exception e) {}
        }
        
        for (int i = 1; i <= nbEcrivains; i++) {
            Thread ecrivain = new Thread(new Ecrivain(i, ressource, this, gui));
            ecrivain.start();
            threads.add(ecrivain);
            gui.ajouterMessage("Ecrivain " + i + " demarre");
            try { Thread.sleep(500); } catch (Exception e) {}
        }
        
        gui.ajouterMessage("");
        gui.ajouterMessage(">>> SIMULATION EN COURS (" + (dureeSimulationMs/1000) + " secondes) <<<");
        
        try {
            Thread.sleep(dureeSimulationMs);
        } catch (Exception e) {}
        
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
            try {
                t.join(1000);
            } catch (Exception e) {}
        }
        
        gui.ajouterMessage("");
        gui.ajouterMessage("=== STATISTIQUES FINALES ===");
        gui.ajouterMessage("Lectures: " + Lecteur.getCompteurLectures());
        gui.ajouterMessage("Ecritures: " + Ecrivain.getCompteurEcritures());
        gui.ajouterMessage("Valeur finale: " + ressource.getDonnee());
        
        gui.majLectures(Lecteur.getCompteurLectures());
        gui.majEcritures(Ecrivain.getCompteurEcritures());
    }
    
    public boolean estFini() {
        return fini;
    }
}
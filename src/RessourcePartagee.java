import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RessourcePartagee {
    
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private String donnee = "Initial";
    private InterfaceGraphique gui;
    private static AtomicInteger compteurFile = new AtomicInteger(0);
    private static long debutSimulation = 0;
    private Random rand = new Random();
    private static AtomicInteger lecteursActifs = new AtomicInteger(0);
    
    public RessourcePartagee(InterfaceGraphique gui) {
        this.gui = gui;
        debutSimulation = System.currentTimeMillis();
    }
    
    private String getTemps() {
        long temps = System.currentTimeMillis() - debutSimulation;
        long secondes = temps / 1000;
        long millis = temps % 1000;
        return String.format("[%02d:%03d]", secondes, millis);
    }
    
    // Methode appelee par le lecteur pour demander l'acces
    public int demanderLecture(int id) {
        int position = compteurFile.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [FILE] Lecteur " + id + " demande acces (position " + position + ")");
        return position;
    }
    
    // Methode appelee par l'ecrivain pour demander l'acces
    public int demanderEcriture(int id) {
        int position = compteurFile.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [FILE] Ecrivain " + id + " demande acces (position " + position + ")");
        return position;
    }
    
    public synchronized void lire(int id, int position) {
        
        
        int actifs = lecteursActifs.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [SORTIE] Lecteur " + id + " obtient acces (pos " + position + ") [lecteurs actifs: " + actifs + "]");
        
        long debut = System.currentTimeMillis();
        gui.ajouterMessage(getTemps() + " [DEBUT] Lecteur " + id + " lit");
        
        int duree = 100 + rand.nextInt(100);
        try { Thread.sleep(duree); } catch (Exception e) {}
        
        long dureeReelle = System.currentTimeMillis() - debut;
        
        gui.ajouterMessage(getTemps() + " [LECTURE] Lecteur " + id + " lit: " + donnee + " (" + dureeReelle + "ms)");
        
        
        actifs = lecteursActifs.decrementAndGet();
        gui.ajouterMessage(getTemps() + " [FIN] Lecteur " + id + " fin [lecteurs actifs: " + actifs + "]\n");
    }
    
    public synchronized void ecrire(int id, int position, String valeur) {
        
        
        gui.ajouterMessage(getTemps() + " [SORTIE] Ecrivain " + id + " obtient acces (pos " + position + ")");
        
        long debut = System.currentTimeMillis();
        gui.ajouterMessage(getTemps() + " [DEBUT] Ecrivain " + id + " ecrit");
        
        int duree = 150 + rand.nextInt(100);
        try { Thread.sleep(duree); } catch (Exception e) {}
        
        long dureeReelle = System.currentTimeMillis() - debut;
        
        gui.ajouterMessage(getTemps() + " [ECRITURE] Ecrivain " + id + " ecrit: " + valeur + " (" + dureeReelle + "ms)");
        donnee = valeur;
        gui.majValeur(valeur);
        
        
        gui.ajouterMessage(getTemps() + " [FIN] Ecrivain " + id + " fin\n");
    }
    
    public String getDonnee() {
        return donnee;
    }
}

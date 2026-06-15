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
        gui.ajouterMessage("Ressource initialisee: " + donnee);
    }
    
    private String getTemps() {
        long temps = System.currentTimeMillis() - debutSimulation;
        long secondes = temps / 1000;
        long millis = temps % 1000;
        return String.format("[%02d:%03d]", secondes, millis);
    }
    
    public void lire(int id) {
        // DELAI TRES COURT pour que les lecteurs arrivent presque en meme temps
        try { Thread.sleep(rand.nextInt(50)); } catch (Exception e) {}
        
        int position = compteurFile.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [FILE] Lecteur " + id + " entre (pos " + position + ")");
        
        rwLock.readLock().lock();
        
        int actifs = lecteursActifs.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [SORTIE] Lecteur " + id + " sort (pos " + position + ") --- LECTEURS ACTIFS: " + actifs + " ---");
        
        long debut = System.currentTimeMillis();
        gui.ajouterMessage(getTemps() + " [DEBUT] Lecteur " + id + " lit");
        
        // Lecture plus longue (200-400ms) pour bien voir le chevauchement
        int duree = 200 + rand.nextInt(200);
        try { Thread.sleep(duree); } catch (Exception e) {}
        
        long dureeReelle = System.currentTimeMillis() - debut;
        
        gui.ajouterMessage(getTemps() + " [LECTURE] Lecteur " + id + " lit: " + donnee + " (" + dureeReelle + "ms)");
        
        rwLock.readLock().unlock();
        actifs = lecteursActifs.decrementAndGet();
        gui.ajouterMessage(getTemps() + " [FIN] Lecteur " + id + " fin --- LECTEURS ACTIFS: " + actifs + " ---\n");
    }
    
    public void ecrire(int id, String valeur) {
        // Delai aleatoire avant d'entrer
        try { Thread.sleep(rand.nextInt(300)); } catch (Exception e) {}
        
        int position = compteurFile.incrementAndGet();
        gui.ajouterMessage(getTemps() + " [FILE] Ecrivain " + id + " entre (pos " + position + ")");
        
        rwLock.writeLock().lock();
        
        gui.ajouterMessage(getTemps() + " [SORTIE] Ecrivain " + id + " sort (pos " + position + ")");
        
        long debut = System.currentTimeMillis();
        gui.ajouterMessage(getTemps() + " [DEBUT] Ecrivain " + id + " ecrit");
        
        int duree = 150 + rand.nextInt(150);
        try { Thread.sleep(duree); } catch (Exception e) {}
        
        long dureeReelle = System.currentTimeMillis() - debut;
        
        gui.ajouterMessage(getTemps() + " [ECRITURE] Ecrivain " + id + " ecrit: " + valeur + " (" + dureeReelle + "ms)");
        donnee = valeur;
        gui.majValeur(valeur);
        
        rwLock.writeLock().unlock();
        gui.ajouterMessage(getTemps() + " [FIN] Ecrivain " + id + " fin\n");
    }
    
    public String getDonnee() {
        return donnee;
    }
}

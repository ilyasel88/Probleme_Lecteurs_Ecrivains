import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RessourcePartagee {
    
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private String donnee = "Initial";
    private InterfaceGraphique gui;
    
    public RessourcePartagee(InterfaceGraphique gui) {
        this.gui = gui;
        gui.ajouterMessage("Ressource initialisee: " + donnee);
    }
    
    public void lire(int id) {
        rwLock.readLock().lock();
        gui.ajouterMessage("Lecteur " + id + " lit: " + donnee);
        rwLock.readLock().unlock();
        
        try { Thread.sleep(100); } catch (Exception e) {}
    }
    
    public void ecrire(int id, String valeur) {
        rwLock.writeLock().lock();
        gui.ajouterMessage("Ecrivain " + id + " ecrit: " + valeur);
        donnee = valeur;
        gui.majValeur(valeur);
        rwLock.writeLock().unlock();
        
        try { Thread.sleep(300); } catch (Exception e) {}
    }
    
    public String getDonnee() {
        return donnee;
    }
}
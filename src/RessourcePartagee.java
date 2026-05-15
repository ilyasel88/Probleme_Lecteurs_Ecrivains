import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RessourcePartagee {
    
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private String donnee = "Initial";
    private InterfaceGraphique gui;
    private static int compteurFile = 0;
    
    public RessourcePartagee(InterfaceGraphique gui) {
        this.gui = gui;
        gui.ajouterMessage("Ressource initialisee: " + donnee);
    }
    
    public void lire(int id) {
        int position;
        synchronized(RessourcePartagee.class) {
            compteurFile++;
            position = compteurFile;
        }
        gui.ajouterMessage("[FILE] Lecteur " + id + " entre en file (position " + position + ")");
        
        rwLock.readLock().lock();
        
        gui.ajouterMessage("[SORTIE] Lecteur " + id + " sort de la file et lit");
        gui.ajouterMessage("[LECTURE] Lecteur " + id + " lit: " + donnee);
        
        try { Thread.sleep(100); } catch (Exception e) {}
        
        rwLock.readLock().unlock();
        gui.ajouterMessage("[FIN] Lecteur " + id + " a termine\n");
    }
    
    public void ecrire(int id, String valeur) {
        int position;
        synchronized(RessourcePartagee.class) {
            compteurFile++;
            position = compteurFile;
        }
        gui.ajouterMessage("[FILE] Ecrivain " + id + " entre en file (position " + position + ")");
        
        rwLock.writeLock().lock();
        
        gui.ajouterMessage("[SORTIE] Ecrivain " + id + " sort de la file et ecrit");
        gui.ajouterMessage("[ECRITURE] Ecrivain " + id + " ecrit: " + valeur);
        donnee = valeur;
        gui.majValeur(valeur);
        
        try { Thread.sleep(300); } catch (Exception e) {}
        
        rwLock.writeLock().unlock();
        gui.ajouterMessage("[FIN] Ecrivain " + id + " a termine\n");
    }
    
    public String getDonnee() {
        return donnee;
    }
}

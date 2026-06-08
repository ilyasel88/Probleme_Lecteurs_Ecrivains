import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe Log — Enregistrement des simulations dans un fichier .log
 *
 * Chaque simulation crée un fichier horodaté dans le dossier logs/ :
 *   logs/simulation_2026-06-08_11-10-57.log
 */
public class Log {

    private static final DateTimeFormatter FMT_FICHIER    =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter FMT_HORODATAGE =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FMT_LISIBLE    =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static BufferedWriter writer  = null;
    private static String         fichier = null;
    private static final String   DOSSIER_LOGS = "logs";

    // -----------------------------------------------------------------------
    // API publique
    // -----------------------------------------------------------------------

    /**
     * Initialise un nouveau fichier de log pour la simulation courante.
     * Crée l'en-tête avec la configuration de la simulation.
     * Doit être appelé une fois au démarrage, avant de créer le Simulateur.
     *
     * @param nbLecteurs    nombre de lecteurs configurés
     * @param nbEcrivains   nombre d'écrivains configurés
     * @param dureeSecondes durée de la simulation en secondes
     */
    public static synchronized void initialiser(int nbLecteurs, int nbEcrivains, int dureeSecondes) {
        fermerSilencieux(); // ferme l'éventuel log précédent

        // Creer le dossier logs s'il n'existe pas
        File dossier = new File(DOSSIER_LOGS);
        if (!dossier.exists()) {
            boolean cree = dossier.mkdir();
            if (cree) {
                System.out.println("[Log] Dossier 'logs' cree");
            } else {
                System.err.println("[Log] Impossible de creer le dossier 'logs'");
            }
        }

        String horodatage = LocalDateTime.now().format(FMT_FICHIER);
        fichier = DOSSIER_LOGS + File.separator + "simulation_" + horodatage + ".log";

        try {
            writer = new BufferedWriter(new FileWriter(fichier, false));
            ecrireLigne("========================================");
            ecrireLigne("   SIMULATION LECTEURS-ECRIVAINS");
            ecrireLigne("   Demarree le : " + LocalDateTime.now().format(FMT_LISIBLE));
            ecrireLigne("----------------------------------------");
            ecrireLigne("   Lecteurs   : " + nbLecteurs);
            ecrireLigne("   Ecrivains  : " + nbEcrivains);
            ecrireLigne("   Duree      : " + dureeSecondes + " secondes");
            ecrireLigne("========================================");
            ecrireLigne("");
        } catch (IOException e) {
            System.err.println("[Log] Impossible de creer le fichier : " + fichier);
            System.err.println("[Log] Cause : " + e.getMessage());
            writer = null;
        }
    }

    /**
     * Enregistre un message horodaté dans le fichier de log.
     * Thread-safe : peut être appelé depuis n'importe quel thread.
     * Les lignes vides sont transmises telles quelles (sans horodatage).
     *
     * @param message le texte à enregistrer
     */
    public static synchronized void enregistrer(String message) {
        if (writer == null) return;

        if (message == null || message.trim().isEmpty()) {
            ecrireLigne("");
        } else {
            String horodatage = LocalDateTime.now().format(FMT_HORODATAGE);
            ecrireLigne("[" + horodatage + "] " + message);
        }
    }

    /**
     * Enregistre les statistiques finales et ferme le fichier proprement.
     * Doit être appelé à la fin de chaque simulation, dans Simulateur.arreter().
     *
     * @param nbLectures   nombre total de lectures effectuées
     * @param nbEcritures  nombre total d'écritures effectuées
     * @param valeurFinale valeur finale de la ressource partagée
     */
    public static synchronized void fermerAvecStatistiques(
            int nbLectures, int nbEcritures, String valeurFinale) {

        if (writer == null) return;

        ecrireLigne("");
        ecrireLigne("========================================");
        ecrireLigne("   STATISTIQUES FINALES");
        ecrireLigne("----------------------------------------");
        ecrireLigne("   Lectures    : " + nbLectures);
        ecrireLigne("   Ecritures   : " + nbEcritures);
        ecrireLigne("   Valeur      : " + valeurFinale);
        ecrireLigne("========================================");
        ecrireLigne("   Terminee le : " + LocalDateTime.now().format(FMT_LISIBLE));
        ecrireLigne("========================================");

        String f = fichier;
        fermerSilencieux();
        System.out.println("[Log] Simulation enregistree dans : " + f);
    }

    /**
     * Retourne le nom du fichier de log actuellement ouvert,
     * ou null si aucun fichier n'est ouvert.
     */
    public static synchronized String getFichier() {
        return fichier;
    }

    // -----------------------------------------------------------------------
    // Méthodes internes
    // -----------------------------------------------------------------------

    /** Ferme le writer sans écrire de statistiques. */
    private static void fermerSilencieux() {
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ignored) {
            } finally {
                writer = null;
            }
        }
    }

    /** Écrit une ligne brute dans le fichier (sans horodatage automatique). */
    private static void ecrireLigne(String ligne) {
        try {
            writer.write(ligne);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("[Log] Erreur d'ecriture : " + e.getMessage());
        }
    }
}

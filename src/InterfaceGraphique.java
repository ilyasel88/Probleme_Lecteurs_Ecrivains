import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceGraphique extends JFrame {
    
    private JLabel lblValeur;
    private JLabel lblLectures;
    private JLabel lblEcritures;
    private JTextArea zoneLogs;
    private JButton btnDemarrer;
    private JButton btnArreter;
    private JProgressBar barreLecteurs;
    private JProgressBar barreEcrivains;
    private JSpinner spinnerLecteurs;
    private JSpinner spinnerEcrivains;
    private JSpinner spinnerDuree;
    private JSplitPane splitPane;
    
    private Simulateur simulateur;
    private Timer timer;
    
    public InterfaceGraphique() {
        initComponents();
        setTitle("Simulateur Lecteurs-Ecrivains");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelConfig = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelConfig.setBorder(BorderFactory.createTitledBorder("Configuration"));
        
        panelConfig.add(new JLabel("Lecteurs:"));
        spinnerLecteurs = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        panelConfig.add(spinnerLecteurs);
        
        panelConfig.add(new JLabel("Ecrivains:"));
        spinnerEcrivains = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        panelConfig.add(spinnerEcrivains);
        
        panelConfig.add(new JLabel("Duree (secondes):"));
        spinnerDuree = new JSpinner(new SpinnerNumberModel(5, 1, 30, 1));
        panelConfig.add(spinnerDuree);
        
        JPanel panelHaut = new JPanel(new GridLayout(2, 2, 10, 10));
        panelHaut.setBorder(BorderFactory.createTitledBorder("Etat de la ressource"));
        panelHaut.setPreferredSize(new Dimension(800, 100));
        
        lblValeur = new JLabel("Valeur: Initial", SwingConstants.CENTER);
        lblValeur.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblLectures = new JLabel("Lectures: 0", SwingConstants.CENTER);
        lblLectures.setFont(new Font("Arial", Font.BOLD, 14));
        lblLectures.setForeground(Color.BLUE);
        
        lblEcritures = new JLabel("Ecritures: 0", SwingConstants.CENTER);
        lblEcritures.setFont(new Font("Arial", Font.BOLD, 14));
        lblEcritures.setForeground(Color.RED);
        
        barreLecteurs = new JProgressBar(0, 100);
        barreLecteurs.setStringPainted(true);
        barreLecteurs.setForeground(Color.BLUE);
        
        barreEcrivains = new JProgressBar(0, 100);
        barreEcrivains.setStringPainted(true);
        barreEcrivains.setForeground(Color.RED);
        
        panelHaut.add(lblValeur);
        panelHaut.add(barreLecteurs);
        panelHaut.add(lblLectures);
        panelHaut.add(barreEcrivains);
        
        JPanel panelLogs = new JPanel(new BorderLayout());
        panelLogs.setBorder(BorderFactory.createTitledBorder("Journal des actions"));
        
        zoneLogs = new JTextArea();
        zoneLogs.setEditable(false);
        zoneLogs.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(zoneLogs);
        panelLogs.add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBas.setBorder(BorderFactory.createTitledBorder("Controles"));
        
        btnDemarrer = new JButton("Demarrer");
        btnDemarrer.setBackground(Color.GREEN);
        btnDemarrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                demarrerSimulation();
            }
        });
        
        btnArreter = new JButton("Arreter");
        btnArreter.setBackground(Color.RED);
        btnArreter.setEnabled(false);
        btnArreter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                arreterSimulation();
            }
        });
        
        JButton btnEffacer = new JButton("Effacer logs");
        btnEffacer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoneLogs.setText("");
            }
        });
        
        panelBas.add(btnDemarrer);
        panelBas.add(btnArreter);
        panelBas.add(btnEffacer);
        
        JPanel panelNord = new JPanel(new BorderLayout());
        panelNord.add(panelConfig, BorderLayout.NORTH);
        panelNord.add(panelHaut, BorderLayout.CENTER);
        
        JPanel panelSud = new JPanel(new BorderLayout());
        panelSud.add(panelLogs, BorderLayout.CENTER);
        panelSud.add(panelBas, BorderLayout.SOUTH);
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelNord, panelSud);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerLocation(150);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void demarrerSimulation() {
        int nbLecteurs    = (Integer) spinnerLecteurs.getValue();
        int nbEcrivains   = (Integer) spinnerEcrivains.getValue();
        int dureeSecondes = (Integer) spinnerDuree.getValue();
        int dureeMs       = dureeSecondes * 1000;
        
        btnDemarrer.setEnabled(false);
        btnArreter.setEnabled(true);

        // --- LOG : initialisation du fichier pour cette simulation ---
        Log.initialiser(nbLecteurs, nbEcrivains, dureeSecondes);

        zoneLogs.setText("");
        ajouterMessage("=== SIMULATION DEMARREE ===");
        ajouterMessage("Configuration: " + nbLecteurs + " lecteurs, "
                + nbEcrivains + " ecrivains, " + dureeSecondes + " secondes");

        // Affiche le nom du fichier log dans le journal graphique
        String fichierLog = Log.getFichier();
        if (fichierLog != null) {
            ajouterMessage("Fichier log : " + fichierLog);
        }
        
        final int finalNbLecteurs  = nbLecteurs;
        final int finalNbEcrivains = nbEcrivains;
        final int finalDureeMs     = dureeMs;
        
        new Thread(new Runnable() {
            public void run() {
                simulateur = new Simulateur(
                        InterfaceGraphique.this,
                        finalNbLecteurs,
                        finalNbEcrivains,
                        finalDureeMs);
                simulateur.demarrer();
            }
        }).start();
        
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mettreAJourBarres();
            }
        });
        timer.start();
    }
    
    private void arreterSimulation() {
        if (simulateur != null) {
            simulateur.arreter();
        }
        btnDemarrer.setEnabled(true);
        btnArreter.setEnabled(false);
        if (timer != null) {
            timer.stop();
        }
        ajouterMessage("=== SIMULATION ARRETEE ===");
    }
    
    private void mettreAJourBarres() {
        int lectures  = Lecteur.getCompteurLectures();
        int ecritures = Ecrivain.getCompteurEcritures();
        int total = lectures + ecritures;
        
        if (total > 0) {
            barreLecteurs.setValue(Math.min(lectures, 100));
            barreLecteurs.setString("Lectures: " + lectures);
            barreEcrivains.setValue(Math.min(ecritures, 100));
            barreEcrivains.setString("Ecritures: " + ecritures);
        }
        
        lblLectures.setText("Lectures: " + lectures);
        lblEcritures.setText("Ecritures: " + ecritures);
    }
    
    /**
     * Ajoute un message dans le journal graphique ET l'enregistre dans le fichier log.
     */
    public void ajouterMessage(String message) {
        // --- LOG : enregistrement sur disque (thread-safe, hors EDT) ---
        Log.enregistrer(message);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                zoneLogs.append(message + "\n");
                zoneLogs.setCaretPosition(zoneLogs.getDocument().getLength());
            }
        });
    }
    
    public void majValeur(String valeur) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lblValeur.setText("Valeur: " + valeur);
            }
        });
    }
    
    public void majLectures(int lectures) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lblLectures.setText("Lectures: " + lectures);
            }
        });
    }
    
    public void majEcritures(int ecritures) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lblEcritures.setText("Ecritures: " + ecritures);
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InterfaceGraphique().setVisible(true);
            }
        });
    }
}

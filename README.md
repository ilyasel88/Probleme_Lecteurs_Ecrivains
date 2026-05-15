# 📚 Simulateur Lecteurs-Écrivains

Simulation du problème classique de **synchronisation concurrente** Lecteurs-Écrivains, avec une interface graphique Swing, une gestion des verrous via `ReentrantReadWriteLock` et un **enregistrement automatique des simulations dans des fichiers log horodatés**.

---

## 🎯 Description

Ce projet implémente une simulation du problème des **Lecteurs-Écrivains** en Java, un problème fondamental en programmation concurrente. Plusieurs threads accèdent simultanément à une ressource partagée selon les règles suivantes :

- **Plusieurs lecteurs** peuvent lire en même temps (accès concurrent en lecture).
- **Un seul écrivain** peut écrire à la fois, et l'écriture est exclusive (aucun lecteur ni autre écrivain).
- L'équité est garantie par un verrou **fair** (`ReentrantReadWriteLock(true)`), évitant la famine.

Une interface graphique permet de configurer, lancer, suivre et arrêter la simulation en temps réel. Chaque simulation est automatiquement **enregistrée dans un fichier log horodaté** généré dans le répertoire d'exécution.

---

## 🖼️ Interface graphique

- **Configuration** : nombre de lecteurs, d'écrivains, et durée de la simulation.
- **État de la ressource** : valeur courante, compteurs de lectures/écritures, barres de progression.
- **Journal des actions** : log en temps réel de chaque accès à la ressource (entrée en file, lecture, écriture, sortie).
- **Contrôles** : boutons Démarrer / Arrêter / Effacer les logs.

---

## 🗂️ Structure du projet

```
Probleme_Lecteurs_Ecrivains/
├── src/
│   ├── Main.java               # Point d'entrée de l'application
│   ├── InterfaceGraphique.java # Interface Swing (GUI) + appels Log
│   ├── Simulateur.java         # Orchestration des threads + fermeture Log
│   ├── RessourcePartagee.java  # Ressource protégée par ReentrantReadWriteLock
│   ├── Lecteur.java            # Thread lecteur
│   ├── Ecrivain.java           # Thread écrivain
│   ├── Log.java                # Enregistrement des simulations sur disque
│   └── makefile                # Compilation et exécution
└── README.md
```

---

## 📥 Installation & Clonage

```bash
# Cloner le dépôt
git clone https://github.com/ilyasel88/Probleme_Lecteurs_Ecrivains.git

# Se placer dans le dossier src
cd Probleme_Lecteurs_Ecrivains/src
```

---

## ⚙️ Prérequis

- **Java JDK 8** ou supérieur
- **Make** (pour utiliser le Makefile)

---

## 🚀 Compilation et exécution

### Avec Make

```bash
# Compiler et lancer directement
make run

# Compiler seulement
make compile

# Nettoyer les fichiers compilés
make clean
```

### Sans Make

```bash
mkdir -p bin
javac -d bin Main.java InterfaceGraphique.java Simulateur.java RessourcePartagee.java Lecteur.java Ecrivain.java Log.java
java -cp bin Main
```

---

## 🔧 Fonctionnement technique

### `RessourcePartagee`
Utilise un `ReentrantReadWriteLock(true)` (mode fair) pour garantir l'équité entre lecteurs et écrivains. Chaque accès (lecture ou écriture) est loggé avec un numéro de position dans la file d'attente.

### `Lecteur` / `Ecrivain`
Chaque thread boucle jusqu'à la fin de la simulation, avec une pause aléatoire entre chaque accès pour simuler un comportement réaliste. Les compteurs globaux sont synchronisés.

### `Simulateur`
Lance tous les threads, attend la durée configurée, puis les interrompt proprement via `interrupt()` et `join()`. Affiche les statistiques finales et déclenche la fermeture du fichier log à la fin.

### `InterfaceGraphique`
Toutes les mises à jour de l'UI se font via `SwingUtilities.invokeLater()` pour respecter le thread Swing (EDT). Chaque appel à `ajouterMessage()` enregistre simultanément le message dans le fichier log.

### `Log`
Classe utilitaire statique et thread-safe. À chaque démarrage de simulation, elle crée un fichier horodaté du type `simulation_2026-05-15_14-32-07.log`. Tous les événements affichés dans le journal graphique sont automatiquement persistés sur disque. En fin de simulation, les statistiques finales sont écrites avant la fermeture propre du fichier.

---

## 📁 Fichiers log générés

Chaque simulation produit un fichier log dans le répertoire d'exécution (`src/`) :

```
simulation_2026-05-15_14-32-07.log
```

Exemple de contenu :

```
========================================
   SIMULATION LECTEURS-ECRIVAINS
   Demarree le : 15/05/2026 14:32:07
----------------------------------------
   Lecteurs   : 3
   Ecrivains  : 2
   Duree      : 10 secondes
========================================

[14:32:07.312] === SIMULATION DEMARREE ===
[14:32:07.315] Configuration: 3 lecteurs, 2 ecrivains, 10 secondes
[14:32:07.420] [DEMARRE] Lecteur 1
[14:32:07.621] [DEMARRE] Lecteur 2
...
[14:32:09.105] [FILE] Lecteur 1 entre en file (position 1)
[14:32:09.106] [SORTIE] Lecteur 1 sort de la file et lit
[14:32:09.107] [LECTURE] Lecteur 1 lit: Initial
[14:32:09.208] [FIN] Lecteur 1 a termine
[14:32:10.340] [FILE] Ecrivain 1 entre en file (position 2)
[14:32:10.341] [SORTIE] Ecrivain 1 sort de la file et ecrit
[14:32:10.342] [ECRITURE] Ecrivain 1 ecrit: Donnee-1-par-1
[14:32:10.643] [FIN] Ecrivain 1 a termine
...
========================================
   STATISTIQUES FINALES
----------------------------------------
   Lectures    : 12
   Ecritures   : 4
   Valeur      : Donnee-4-par-2
========================================
   Terminee le : 15/05/2026 14:32:17
========================================
```

---

## 📊 Exemple de sortie (journal graphique)

```
=== SIMULATION DEMARREE ===
Configuration: 3 lecteurs, 2 ecrivains, 10 secondes
Fichier log : simulation_2026-05-15_14-32-07.log
Ressource initialisee: Initial
[DEMARRE] Lecteur 1
...
[FILE] Lecteur 1 entre en file (position 1)
[SORTIE] Lecteur 1 sort de la file et lit
[LECTURE] Lecteur 1 lit: Initial
[FIN] Lecteur 1 a termine

[FILE] Ecrivain 1 entre en file (position 2)
[SORTIE] Ecrivain 1 sort de la file et ecrit
[ECRITURE] Ecrivain 1 ecrit: Donnee-1-par-1
[FIN] Ecrivain 1 a termine

=== STATISTIQUES FINALES ===
Lectures  : 12
Ecritures : 4
Valeur finale : Donnee-4-par-2
```

---

## 📚 Concepts illustrés

- Programmation multi-thread en Java (`Thread`, `Runnable`)
- Synchronisation avec `ReentrantReadWriteLock`
- Problème des Lecteurs-Écrivains (concurrence, exclusion mutuelle, équité)
- Interface graphique Swing avec mise à jour thread-safe
- Utilisation d'un `Timer` Swing pour les mises à jour périodiques
- Journalisation persistante thread-safe avec horodatage (`Log.java`)

---

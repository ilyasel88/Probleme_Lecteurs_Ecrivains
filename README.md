# 📚 Simulateur Lecteurs-Écrivains

Simulation du problème classique de **synchronisation concurrente** Lecteurs-Écrivains, avec une interface graphique Swing et une gestion des verrous via `ReentrantReadWriteLock`.

---

## 🎯 Description

Ce projet implémente une simulation du problème des **Lecteurs-Écrivains** en Java, un problème fondamental en programmation concurrente. Plusieurs threads accèdent simultanément à une ressource partagée selon les règles suivantes :

- **Plusieurs lecteurs** peuvent lire en même temps (accès concurrent en lecture).
- **Un seul écrivain** peut écrire à la fois, et l'écriture est exclusive (aucun lecteur ni autre écrivain).
- L'équité est garantie par un verrou **fair** (`ReentrantReadWriteLock(true)`), évitant la famine.

Une interface graphique permet de configurer, lancer, suivre et arrêter la simulation en temps réel.

---

## 🖼️ Interface graphique

- **Configuration** : nombre de lecteurs, d'écrivains, et durée de la simulation.
- **État de la ressource** : valeur courante, compteurs de lectures/écritures, barres de progression.
- **Journal des actions** : log en temps réel de chaque accès à la ressource (entrée en file, lecture, écriture, sortie).
- **Contrôles** : boutons Démarrer / Arrêter / Effacer les logs.

---

## 🗂️ Structure du projet

```
.
├── Main.java               # Point d'entrée de l'application
├── InterfaceGraphique.java # Interface Swing (GUI)
├── Simulateur.java         # Orchestration des threads
├── RessourcePartagee.java  # Ressource protégée par ReentrantReadWriteLock
├── Lecteur.java            # Thread lecteur
├── Ecrivain.java           # Thread écrivain
└── makefile                # Compilation et exécution
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
javac -d bin Main.java InterfaceGraphique.java Simulateur.java RessourcePartagee.java Lecteur.java Ecrivain.java
java -cp bin Main
```

---

## 🔧 Fonctionnement technique

### `RessourcePartagee`
Utilise un `ReentrantReadWriteLock(true)` (mode fair) pour garantir l'équité entre lecteurs et écrivains. Chaque accès (lecture ou écriture) est loggé avec un numéro de position dans la file d'attente.

### `Lecteur` / `Ecrivain`
Chaque thread boucle jusqu'à la fin de la simulation, avec une pause aléatoire entre chaque accès pour simuler un comportement réaliste. Les compteurs globaux sont synchronisés.

### `Simulateur`
Lance tous les threads, attend la durée configurée, puis les interrompt proprement via `interrupt()` et `join()`. Affiche les statistiques finales à la fin.

### `InterfaceGraphique`
Toutes les mises à jour de l'UI se font via `SwingUtilities.invokeLater()` pour respecter le thread Swing (EDT).

---

## 📊 Exemple de sortie (journal)

```
=== SIMULATION DEMARREE ===
Configuration: 3 lecteurs, 2 ecrivains, 10 secondes
Ressource initialisee: Initial
Lecteur 1 demarre
...
[FILE] Lecteur 1 entre en file (position 1)
[LECTURE] Lecteur 1 lit: Initial
[SORTIE] Lecteur 1 sort de la file
[FILE] Ecrivain 1 entre en file (position 2)
[ECRITURE] Ecrivain 1 ecrit: Donnee-1-par-1
[SORTIE] Ecrivain 1 sort de la file
...
=== STATISTIQUES FINALES ===
Lectures: 12
Ecritures: 4
Valeur finale: Donnee-4-par-2
```

---

## 📚 Concepts illustrés

- Programmation multi-thread en Java (`Thread`, `Runnable`)
- Synchronisation avec `ReentrantReadWriteLock`
- Problème des Lecteurs-Écrivains (concurrence, exclusion mutuelle, équité)
- Interface graphique Swing avec mise à jour thread-safe
- Utilisation d'un `Timer` Swing pour les mises à jour périodiques

---

## 👥 Auteurs

Projet réalisé dans le cadre d'un cours de **systèmes et programmation concurrente**.

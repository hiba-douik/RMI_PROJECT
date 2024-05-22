# RMI Screen Sharing and Interaction Project

## Description
Ce projet implémente une application de partage d'écran utilisant Java RMI (Remote Method Invocation). Il permet à un client de visualiser l'écran d'un serveur distant en temps réel, ainsi que de suivre les mouvements de la souris.

## Fonctionnalités
- Capture d'écran du serveur et envoi au client.
- Affichage en temps réel de l'écran du serveur sur le client.
- Suivi des mouvements de la souris sur l'écran partagé.

## Structure du Projet
Le projet est divisé en plusieurs classes :

### Serveur
- `ScreenSharingImpl` : Implémentation de l'interface RMI pour capturer l'écran et obtenir la position de la souris.
- `Server` : Classe principale pour démarrer le serveur RMI.

### Client
- `ScreenSharingClient` : Classe principale pour le client qui demande des captures d'écran au serveur et affiche l'écran partagé avec la position de la souris.

### Interface
- `ScreenSharingInterface` : Interface RMI définissant les méthodes disponibles pour le partage d'écran.

## Prérequis
- Java Development Kit (JDK) 8 ou supérieur
- Git

## Installation et Exécution

### Cloner le dépôt
```bash
git clone https://github.com/hiba-douik/RMI_PROJECT.git
cd RMI_PROJECT

# TP Dev Avancé #2 - Modernisation avec JPA/Hibernate

Ce projet est une refonte de l'application "MasterAnnonce". L'objectif était de remplacer l'ancienne gestion de base de données (JDBC) par une approche plus moderne utilisant JPA et Hibernate, tout en structurant mieux le code.

## Organisation du code

J'ai découpé l'application en plusieurs couches logiques pour que ce soit plus propre :

- Entity : Ce sont les classes qui représentent nos données (Utilisateurs, Annonces, Catégories).
- Repository : C'est la partie qui dialogue directement avec la base de données (les requêtes).
- Service : C'est le "cerveau" de l'application. Il gère les règles métier et s'assure que les modifications sont bien enregistrées (gestion des transactions).
- Web : Ce sont les pages (JSP) et les contrôleurs (Servlets) qui gèrent l'affichage et les actions de l'utilisateur.

## Difficultés rencontrées et solutions

### 1. Problème d'affichage des données liées

Un souci fréquent avec JPA, c'est qu'il ne charge pas toujours toutes les données d'un coup pour optimiser les performances (par exemple, il charge l'annonce mais pas forcément son auteur tout de suite). Le problème, c'est que la connexion à la base de données est fermée au moment où la page s'affiche, ce qui provoquait une erreur.
Solution : J'ai modifié mes requêtes pour demander explicitement à la base de charger l'auteur et la catégorie en même temps que l'annonce (avec `JOIN FETCH`). Comme ça, toutes les données sont disponibles pour l'affichage.

### 2. Mise à jour des données

Quand on modifie une annonce, on récupère des données qui ne sont plus connectées à la base de données. Il a fallu faire attention à bien "reconnecter" ces données lors de la sauvegarde, surtout pour ne pas perdre le lien avec la catégorie ou l'utilisateur qui a créé l'annonce.

### 3. Gestion des erreurs de saisie

Pour améliorer l'expérience utilisateur, j'ai ajouté des vérifications automatiques. Si un champ obligatoire est manquant, le formulaire se réaffiche avec un message d'erreur clair. J'ai aussi fait en sorte que les champs déjà remplis ne s'effacent pas, pour éviter à l'utilisateur de devoir tout ressaisir.

### 4. Recherche et Pagination

Pour éviter d'avoir une page trop longue, j'ai mis en place un système de pagination et une barre de recherche. J'ai utilisé les fonctionnalités standard de JPA pour ne récupérer que les annonces nécessaires à l'affichage de la page en cours, ce qui est plus efficace.

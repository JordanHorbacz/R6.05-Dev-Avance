# TP Dev Avancé #3 - API REST avec Jersey (dans mon cas)

Ce projet est une évolution de l'application "MasterAnnonce". L'objectif était de transformer le backend en une véritable API REST sécurisée, testée et industrialisable

## Organisation du code

J'ai structuré l'application en plusieurs couches pour respecter une architecture propre et modulaire :

- **API (Resource)** : C'est le point d'entrée de l'API. J'ai utilisé Jersey pour exposer mes ressources (`AnnonceResource`, `LoginResource`). C'est ici que je gère les requêtes HTTP (GET, POST, PUT, DELETE) et la conversion en JSON.
- **Service** : C'est la couche métier. Elle contient toute la logique de l'application (création d'annonce, publication, règles de gestion) et gère les transactions.
- **Repository (DAO)** : Cette couche s'occupe uniquement du dialogue avec la base de données via `EntityManager`.
- **Entity** : Ce sont les objets qui représentent mes tables en base de données (JPA).
- **DTO (Data Transfer Object)** : J'ai créé des objets spécifiques (`AnnonceDTO`) pour les échanges avec le client, afin de ne pas exposer directement mes entités et de mieux contrôler ce qui est envoyé/reçu.
- **Security** : J'ai regroupé ici tout ce qui concerne l'authentification (génération de token) et l'autorisation (filtre de sécurité).

## Difficultés rencontrées et solutions

### 1. Mapping Entité <-> DTO

Un défi a été de convertir proprement mes entités en DTO et inversement sans utiliser de librairie externe lourde comme MapStruct pour cet exercice.
**Solution** : J'ai implémenté un `AnnonceMapper` manuel et utilisé le pattern **Builder** (comme expliqué dans le cours) dans mon DTO. Cela rend le code de création d'objets beaucoup plus lisible et facile à maintenir.

### 2. Mise à jour partielle (PUT)

Lors de la mise à jour d'une annonce, je ne voulais pas écraser les champs existants si le DTO envoyé contenait des valeurs nulles.
**Solution** : J'ai créé une méthode spécifique `updateEntity` dans mon mapper qui vérifie chaque champ. Si une valeur est présente dans le DTO, elle met à jour l'entité ; sinon, elle conserve l'ancienne valeur. C'est une sorte de "patch" manuel.

### 3. Sécurité et Contexte Utilisateur

Il fallait sécuriser l'API sans utiliser de session HTTP classique (stateless). Le problème était aussi de savoir "qui" fait la requête pour vérifier s'il est l'auteur de l'annonce.
**Solution** : J'ai mis en place un système de **Token** (Bearer). J'ai créé un filtre (`AuthenticationFilter`) qui intercepte chaque requête, vérifie le token, et injecte l'identité de l'utilisateur dans le `SecurityContext` de Jersey. Ainsi, dans mes ressources, je peux récupérer l'ID de l'utilisateur courant simplement en injectant le contexte de sécurité.

### 4. Gestion des erreurs standardisée

Par défaut, si une validation échoue ou qu'une erreur survient, le serveur renvoie souvent une stack trace ou un message HTML moche.
**Solution** : J'ai utilisé des `ExceptionMapper`. J'en ai fait un pour les erreurs de validation (400 Bad Request) qui renvoie la liste précise des champs invalides en JSON, et un autre générique (500 Internal Server Error) pour capturer tous les autres plantages et renvoyer un JSON propre au lieu du HTML par défaut de Tomcat.

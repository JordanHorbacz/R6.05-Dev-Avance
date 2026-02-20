# TP Dev Avancé #4 - Migration Spring Boot

Ce projet est la migration finale de l'application "MasterAnnonce" vers une architecture **Spring Boot** complète. L'objectif était de moderniser tout le backend, en passant d'une stack JAX-RS/Servlet manuelle à l'écosystème Spring, tout en renforçant la sécurité, la maintenabilité et l'industrialisation.

## Architecture & Choix Techniques

J'ai refondu l'application en suivant une architecture en couches stricte et moderne :

- **Controller (Web Layer)** : J'ai remplacé les ressources JAX-RS par des `@RestController`. Ils exposent l'API et valident les entrées (`@Valid`). Ils ne manipulent **que des DTOs**.
- **Service (Business Layer)** : Contient toute la logique métier. J'ai utilisé `@Transactional` pour garantir l'intégrité des données et `@PreAuthorize` pour la sécurité fine (RBAC).
- **Repository (Data Layer)** : J'ai supprimé les DAO manuels pour utiliser **Spring Data JPA**. J'ai aussi intégré `JpaSpecificationExecutor` pour permettre une recherche dynamique et performante sans écrire de requêtes JPQL concaténées.
- **Mapper** : J'ai intégré **MapStruct** pour automatiser la conversion Entity <-> DTO, remplaçant le code manuel fastidieux et sensible aux erreurs.
- **Security** : J'ai implémenté une sécurité **Stateless avec JWT** via Spring Security. Une barrière complète (`SecurityFilterChain`, `JwtFilter`) protège l'API.

## Difficultés rencontrées et solutions

### 1. La migration vers Spring Data JPA & Specifications

Passer de requêtes JPQL manuelles à des interfaces vides est magique, mais la gestion des filtres dynamiques (recherche par critères optionnels) était complexe.
**Solution** : J'ai utilisé l'API **Criteria** via des `Specifications`. Cela m'a permis de construire une requête propre et sécurisée (pas d'injection SQL) en empilant les prédicats (filtres) seulement si les paramètres sont présents.

### 2. Le Mapping DTO avec MapStruct

Configurer MapStruct pour qu'il s'intègre bien avec Spring (`componentModel = "spring"`) et gère les relations (ID vers Entité) a demandé un peu de configuration.
**Solution** : J'ai défini des mappers clairs et utilisé l'injection de dépendances pour que les services puissent utiliser les mappers. J'ai gardé les DTOs simples (POJO) pour faciliter le travail de MapStruct.

### 3. La Sécurité JWT

Configurer Spring Security 6 est verbeux (SecurityFilterChain). Il fallait gérer le filtre JWT, l'AuthenticationManager et le UserDetailsService sans créer de dépendances cycliques.
**Solution** : J'ai séparé la logique : un `JwtUtils` pour la crypto pure, un `AuthTokenFilter` pour l'interception HTTP, et une config centralisée. J'ai aussi ajouté un `LoggingAspect` pour tracer les appels, ce qui aide grandement au débogage de la sécurité.

### 4. Tests et Industrialisation

Faire tourner les tests d'intégration avec une vraie base de données en CI était un défi.
**Solution (Choix DB)** : J'ai choisi **Testcontainers** (Option 1). C'est la solution la plus robuste car elle lance une vraie base PostgreSQL jetable Dockerisée pour les tests. Cela garantit que les tests sont iso-prod et ne dépendent pas d'une base installée sur la machine ou la CI.

## Pipeline CI/CD

Le workflow GitHub Actions (.github/workflows/ci.yml) est configuré pour :

1.  Compiler et Tester (Unitaires + Intégration via Testcontainers).
2.  Packager l'application (JAR).
3.  Publier l'artefact.

L'application est également dockerisée via un `Dockerfile` multi-stage optimisé.

## Comment lancer

1.  `docker-compose up -d` (Lance la DB et l'App)
2.  L'API est accessible sur `http://localhost:8080`
3.  Documentation Swagger : `http://localhost:8080/swagger-ui/index.html`

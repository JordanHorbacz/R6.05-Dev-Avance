# TP Dev Avancé #4 — Migration Spring Boot

Ce projet est la suite logique du TP#3. L'objectif était de migrer toute l'application MasterAnnonce vers Spring Boot, en remplaçant Jersey, le filtre JAAS, et la gestion manuelle de JPA par un écosystème Spring cohérent.

## Organisation du code

J'ai gardé la même philosophie de couches que sur le TP#3, mais Spring simplifie beaucoup de choses :

- **Controller** : remplace les ressources Jersey. Avec `@RestController` et `@RequestMapping`, plus besoin de `@Path`, `@Produces`, etc.
- **Service** : la logique métier reste là. Spring gère les transactions avec `@Transactional`, plus besoin de `tx.begin()` / `tx.commit()` à la main.
- **Repository** : remplace les DAO manuels avec `EntityManager`. Une interface qui étend `JpaRepository<Annonce, Long>` et Spring génère toute l'implémentation au démarrage.
- **DAO** : j'ai quand même gardé une couche DAO (interfaces + implémentations) au-dessus des repositories, pour découpler le service de Spring Data JPA. Ça permet en théorie de changer d'implémentation sans toucher aux services.
- **DTO** : comme au TP#3, jamais les entités JPA directement dans les réponses HTTP. J'ai ajouté `AuthResponseDTO`, `UserDTO`, `CategoryDTO` pour couvrir tous les cas.
- **Mapper** : j'ai remplacé le mapping manuel et le pattern Builder du TP#3 par **MapStruct**. Les conversions entité ↔ DTO sont générées à la compilation, c'est beaucoup plus propre.
- **Exception** : j'ai créé des exceptions métier typées (`UserNotFoundException`, `AnnonceNotModifiableException`, etc.) plutôt que de balancer des `RuntimeException` génériques.
- **Security** : `SecurityFilterChain` + filtre JWT custom + `@PreAuthorize`. Remplace le filtre JAAS du TP#3.
- **Aspect** : un `LoggingAspect` en AOP qui intercepte toutes les méthodes de service automatiquement pour logger l'entrée, la sortie, le temps d'exécution et les erreurs.

## Difficultés rencontrées et solutions

### 1. La recherche multi-critères avec Specifications

Le TP imposait de ne pas faire une seule requête JPQL "géante" avec tous les filtres. Le problème c'est qu'avec JPQL, soit tu hard-codes toutes les conditions, soit tu fais de la concaténation de chaînes (dangereux et moche).

**Solution** : l'API `Specifications` de Spring Data JPA + `JpaSpecificationExecutor`. On construit une `Specification<Annonce>` en empilant des prédicats one by one selon quels paramètres sont présents. La requête SQL générée est propre et chaque critère est isolé dans sa propre méthode. J'ai mis tout ça dans une classe `AnnonceSpecifications`.

### 2. Le logging AOP et le lazy loading JPA

Quand on log les arguments d'une méthode de service, si un argument est une entité JPA avec des relations `LAZY`, appeler `toString()` dessus peut déclencher du lazy loading involontaire — hors contexte transactionnel ça explose, dans un contexte ça fait des requêtes SQL non voulues.

**Solution** : dans le `LoggingAspect`, je ne log jamais les objets directement. Je construis une représentation lisible des arguments (nom du param + valeur tronquée à 80 chars), et je masque automatiquement tout ce qui ressemble à un mot de passe ou token.

### 3. Spring Security 6 et les dépendances cycliques

Spring Security 6 a changé beaucoup de choses par rapport à la version 5. La principale : `WebSecurityConfigurerAdapter` n'existe plus, tout doit être en beans avec lambdas. Le vrai problème que j'ai rencontré c'est une dépendance cyclique entre `SecurityConfig` (qui a besoin de `UserDetailsService`), `UserDetailsServiceImpl` (qui injecte `UserRepository`) et `AuthTokenFilter` (qui a besoin de `JwtUtils`).

**Solution** : séparer clairement les responsabilités. `JwtUtils` ne dépend de rien. `AuthTokenFilter` ne dépend que de `JwtUtils` et `UserDetailsService`. `SecurityConfig` instancie `AuthTokenFilter` directement. Pas d'injection croisée.

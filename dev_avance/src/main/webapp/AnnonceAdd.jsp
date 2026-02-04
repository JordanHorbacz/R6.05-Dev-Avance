<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Ajouter une Annonce</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a href="AnnonceList" class="navbar-brand">Petites Annonces</a>
        <div class="nav-links">
            <a href="AnnonceList">Voir les annonces</a>
            <a href="AnnonceAdd">Ajouter une annonce</a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="card">
        <h2>Création d'une annonce</h2>
        <p class="mb-2">Remplissez le formulaire ci-dessous pour publier une nouvelle annonce.</p>

        <c:if test="${param.status == 'success'}">
            <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                Annonce ajoutée avec succès !
            </div>
        </c:if>

        <form action="AnnonceAdd" method="POST">
            <div class="form-group">
                <label for="title">Titre :</label>
                <input type="text" id="title" name="title" placeholder="Titre de l'annonce" required>
            </div>

            <div class="form-group">
                <label for="description">Description :</label>
                <textarea id="description" name="description" placeholder="Description détaillée" required></textarea>
            </div>

            <div class="form-group">
                <label for="adress">Adresse :</label>
                <input type="text" id="adress" name="adress" placeholder="Ville ou adresse" required>
            </div>

            <div class="form-group">
                <label for="mail">Email :</label>
                <input type="email" id="mail" name="mail" placeholder="votre@email.com" required>
            </div>

            <div class="text-center">
                <input type="submit" value="Enregistrer l'annonce" class="btn btn-primary">
            </div>
        </form>
    </div>
</div>

</body>
</html>
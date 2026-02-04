<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Modifier l'annonce</title>
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
        <h2>Modifier l'annonce</h2>
        <p class="mb-2">Modifiez les informations ci-dessous.</p>

        <form method="post" action="AnnonceUpdate">
            <input type="hidden" name="id" value="${annonce.id}">

            <div class="form-group">
                <label for="title">Titre :</label>
                <input type="text" id="title" name="title" value="${annonce.title}" required>
            </div>

            <div class="form-group">
                <label for="description">Description :</label>
                <textarea id="description" name="description" required>${annonce.description}</textarea>
            </div>

            <div class="form-group">
                <label for="adress">Adresse :</label>
                <input type="text" id="adress" name="adress" value="${annonce.adress}" required>
            </div>

            <div class="form-group">
                <label for="mail">Email :</label>
                <input type="email" id="mail" name="mail" value="${annonce.mail}" required>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
                <a href="AnnonceList" class="btn btn-danger" style="margin-left: 10px;">Annuler</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Liste des Annonces</title>
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
    <div class="header-actions">
      <h2>Liste des Annonces</h2>
      <a href="AnnonceAdd" class="btn btn-primary btn-sm">Ajouter une annonce</a>
    </div>

    <c:choose>
      <c:when test="${not empty annonces}">
        <table>
          <thead>
          <tr>
            <th>Titre</th>
            <th>Description</th>
            <th>Adresse</th>
            <th>Email</th>
            <th>Date</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${annonces}" var="a">
            <tr>
              <td><strong>${a.title}</strong></td>
              <td>${a.description}</td>
              <td>${a.adress}</td>
              <td>${a.mail}</td>
              <td>${a.date}</td>
              <td>
                <a href="AnnonceUpdate?id=${a.id}" class="btn btn-primary btn-sm" style="margin-right: 5px;">Modifier</a>
                <a href="AnnonceDelete?id=${a.id}" class="btn btn-danger btn-sm"
                   onclick="return confirm('Voulez-vous vraiment supprimer cette annonce ?');">
                  Supprimer
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:when>
      <c:otherwise>
        <p class="text-center text-muted">Aucune annonce disponible pour le moment.</p>
      </c:otherwise>
    </c:choose>
  </div>
</div>

</body>
</html>
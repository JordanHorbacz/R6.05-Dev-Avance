<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inscription - Dev Avance</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h2>Créer un compte</h2>

    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="register" method="post">
        <label for="username">Nom d'utilisateur :</label>
        <input type="text" id="username" name="username" required>
        <br><br>

        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required>
        <br><br>

        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required>
        <br><br>

        <button type="submit">S'inscrire</button>
    </form>

    <p>Déjà un compte ? <a href="login.jsp">Se connecter</a></p>
</div>
</body>
</html>
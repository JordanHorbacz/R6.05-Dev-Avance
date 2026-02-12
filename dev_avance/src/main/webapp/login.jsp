<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Login - MasterAnnonce</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="container" style="display: flex; justify-content: center; align-items: center; min-height: 80vh;">
        <form action="login" method="post" class="card">
            <h1 style="font-size: 2rem; text-align: center;">Welcome Back</h1>
            
            <c:if test="${not empty error}">
                <div class="error-msg" style="text-align: center; margin-bottom: 1rem;">${error}</div>
            </c:if>

            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" value="${email}" required placeholder="john@example.com">

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="••••••••">

            <button type="submit" class="btn" style="width: 100%;">Sign In</button>

            <p style="text-align: center; font-size: 0.875rem; color: var(--text-muted);">
                Don't have an account? <a href="#" style="color: var(--primary);">Register</a>
            </p>
        </form>
    </div>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Post Annonce - MasterAnnonce</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav>
        <a href="AnnonceList" class="logo">MasterAnnonce</a>
        <div><a href="AnnonceList">Back to List</a></div>
    </nav>

    <div class="container">
        <form action="AnnonceAdd" method="post">
            <h1>Create New Ad</h1>
            
            <c:if test="${not empty errors}">
                <div style="background-color: #fee2e2; border: 1px solid #ef4444; color: #b91c1c; padding: 1rem; border-radius: 0.5rem;">
                    <strong>Please fix the following errors:</strong>
                    <ul style="margin-left: 1.5rem; margin-top: 0.5rem;">
                        <c:forEach var="error" items="${errors}">
                            <li>${error.message} (Field: ${error.propertyPath})</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <label for="title">Title *</label>
            <input type="text" id="title" name="title" value="${annonce.title}" required>

            <label for="categoryId">Category *</label>
            <select name="categoryId" id="categoryId" required>
                <option value="">Select Category</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.id}" ${annonce.category != null && annonce.category.id == cat.id ? 'selected' : ''}>${cat.label}</option>
                </c:forEach>
            </select>

            <label for="description">Description *</label>
            <textarea id="description" name="description" rows="5" required>${annonce.description}</textarea>

            <label for="adress">Address</label>
            <input type="text" id="adress" name="adress" value="${annonce.adress}">

            <label for="mail">Contact Email</label>
            <input type="email" id="mail" name="mail" value="${annonce.mail}">

            <button type="submit" class="btn">Publish Annonce</button>
        </form>
    </div>
</body>
</html>
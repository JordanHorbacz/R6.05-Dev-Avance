<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${annonce.title} - MasterAnnonce</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav>
        <a href="AnnonceList" class="logo">MasterAnnonce</a>
        <div><a href="AnnonceList">Back to List</a></div>
    </nav>

    <div class="container">
        <div class="card" style="max-width: 800px; margin: 0 auto; padding: 2rem;">
            <header style="border-bottom: 1px solid #e5e7eb; padding-bottom: 1rem; margin-bottom: 1rem;">
                <span style="font-size: 0.875rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--secondary); font-weight: 700;">
                    ${annonce.category.label != null ? annonce.category.label : 'Uncategorized'}
                </span>
                <h1 style="font-size: 2.5rem; margin: 0.5rem 0; background: none; -webkit-text-fill-color: initial; color: var(--text-color);">${annonce.title}</h1>
                <div style="display: flex; justify-content: space-between; color: var(--text-muted); font-size: 0.875rem;">
                    <span>By ${annonce.author.username}</span>
                    <span>${annonce.date}</span>
                </div>
            </header>
            
            <div style="font-size: 1.125rem; line-height: 1.7; margin-bottom: 2rem;">
                ${annonce.description}
            </div>

            <div style="background: var(--bg-color); padding: 1.5rem; border-radius: 0.5rem; margin-bottom: 2rem;">
                <h3 style="margin-bottom: 0.5rem; color: var(--text-color);">Contact Info</h3>
                <p><strong>Address:</strong> ${annonce.adress}</p>
                <p><strong>Email:</strong> <a href="mailto:${annonce.mail}" style="color: var(--primary);">${annonce.mail}</a></p>
            </div>

            <c:if test="${sessionScope.user.id == annonce.author.id}">
                <div style="display: flex; justify-content: flex-end; gap: 1rem;">
                    <a href="AnnonceUpdate?id=${annonce.id}" class="btn">Edit Annonce</a>
                    <form action="AnnonceDelete" method="post" style="box-shadow: none; background: none; padding: 0; width: auto; margin: 0;">
                        <input type="hidden" name="id" value="${annonce.id}"/>
                        <button type="submit" class="btn btn-danger" onclick="return confirm('Delete this ad?')" style="padding: 0.75rem 1.5rem;">Delete Annonce</button>
                    </form>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>

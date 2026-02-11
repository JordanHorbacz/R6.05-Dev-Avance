<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Annonces - MasterAnnonce</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav>
        <a href="AnnonceList" class="logo">MasterAnnonce</a>
        <div>
            <a href="AnnonceList">Browse</a>
            <a href="AnnonceAdd" class="btn">Post Ad</a>
            <c:if test="${not empty sessionScope.user}">
                <a href="logout" style="color: var(--error); margin-left: 1rem;">Logout (${sessionScope.user.username})</a>
            </c:if>
        </div>
    </nav>

    <div class="container">
        <header style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h1>Latest Annonces</h1>
            <form action="AnnonceList" method="get" style="flex-direction: row; padding: 0.5rem; background: transparent; box-shadow: none; width: auto; margin: 0;">
                <input type="text" name="search" placeholder="Search..." value="${search}">
                <button type="submit" class="btn" style="padding: 0.5rem 1rem; margin-left: 0.5rem;">Search</button>
            </form>
        </header>

        <div class="grid">
            <c:forEach var="annonce" items="${annonces}">
                <div class="card">
                    <span style="font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--secondary); font-weight: 700;">
                        ${annonce.category.label != null ? annonce.category.label : 'Uncategorized'}
                    </span>
                    <h2 style="font-size: 1.5rem; margin: 0.5rem 0;">${annonce.title}</h2>
                    <p style="color: var(--text-muted); margin-bottom: 1rem; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                        ${annonce.description}
                    </p>
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: auto;">
                        <span style="font-size: 0.875rem; font-weight: 500;">
                            By ${annonce.author.username}
                        </span>
                        <div>
                            <c:if test="${sessionScope.user.id == annonce.author.id}">
                                <a href="AnnonceUpdate?id=${annonce.id}" style="color: var(--primary); font-weight: 600; text-decoration: none; margin-right: 0.5rem;">Edit</a>
                                <form action="AnnonceDelete" method="post" style="display:inline; padding:0; background:none; box-shadow:none; margin:0;">
                                    <input type="hidden" name="id" value="${annonce.id}"/>
                                    <button type="submit" onclick="return confirm('Delete this ad?')" style="color: var(--error); background:none; border:none; cursor:pointer; font-weight:600;">Delete</button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="AnnonceList?page=${currentPage - 1}&search=${search}" class="page-link">Previous</a>
            </c:if>
            <span class="page-link active">${currentPage}</span>
            <a href="AnnonceList?page=${currentPage + 1}&search=${search}" class="page-link">Next</a>
        </div>
    </div>
</body>
</html>
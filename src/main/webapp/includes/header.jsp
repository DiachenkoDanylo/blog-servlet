<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<!-- Navigation Bar -->
<div class="navbar">
    <a href="${pageContext.request.contextPath}/">Home</a>
    <a href="${pageContext.request.contextPath}/recipes">Recipes</a>
    <a href="${pageContext.request.contextPath}/authors">Authors</a>


    <% if (session.getAttribute("user") != null) { %>
    <a href="${pageContext.request.contextPath}/cabinet">My Page</a>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
    <% } else { %>
    <a href="${pageContext.request.contextPath}/login">Login</a>
    <% } %>
</div>
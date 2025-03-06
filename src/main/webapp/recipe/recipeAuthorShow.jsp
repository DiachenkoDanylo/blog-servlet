<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.diachenko.dietblog.model.Recipe" %>
<%@ page import="java.util.List" %>
<%@ page import="com.diachenko.dietblog.model.AppUser" %>
<%
    AppUser appUser = (AppUser) request.getAttribute("author");
    List<Recipe> recipes = null;
    if (request.getAttribute("recipes") != null) {
        recipes = (List<Recipe>) request.getAttribute("recipes");
    }
%>
<%@ include file="/includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title><%=appUser.getUsername()%>
    </title>
</head>
<body>
<div class="recipe-grid">
    <% if (recipes != null && !recipes.isEmpty()) { %>
    <% for (Recipe recipe : recipes) { %>
    <div class="recipe-card">
        <img style="max-width: 400px" src="<%= recipe.getTitleImage() %>" alt="<%= recipe.getTitle() %>">
        <h2><%= recipe.getTitle() %>
        </h2>
        <p><strong>Calories:</strong> <%= recipe.getCalories() %> kcal</p>
        <a href="recipes?id=<%= recipe.getId() %>" class="view-button">View Recipe</a>
    </div>
    <% } %>
    <% } else { %>
    <p>No recipes found.</p>
    <% } %>
</div>
</body>
</html>

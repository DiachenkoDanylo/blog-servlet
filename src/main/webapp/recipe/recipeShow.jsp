<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.diachenko.dietblog.model.Recipe" %>
<%@ page import="com.diachenko.dietblog.model.AppUser" %>
<%
    Recipe recipe = (Recipe) request.getAttribute("recipe");
    AppUser appUser = (AppUser) request.getSession(false).getAttribute("user");
%>
<%@ include file="/includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
<div class="recipe-card">
    <img style="max-width: 400px" src="<%= recipe.getTitleImage() %>" alt="<%= recipe.getTitle() %>">
    <h2><%= recipe.getTitle() %>
    </h2>
    <p><strong>Description:</strong> <%= recipe.getDescription() %>
    </p>
    <p><strong>Calories:</strong> <%= recipe.getCalories() %> kcal</p>
    <a href="authors?id=<%= recipe.getOwner().getId() %>" class="view-button">View Author</a>
    <% if (appUser != null && appUser.equals(recipe.getOwner())) { %>
    <a href="recipes/edit?id=<%= recipe.getId() %>" class="edit-button">Edit Recipe</a>
    <% } %>
</div>
</body>
</html>

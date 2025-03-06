<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.diachenko.dietblog.model.AppUser" %>
<%@ page import="com.diachenko.dietblog.model.Recipe" %>
<%@ page import="java.util.List" %>

<%
    AppUser user = (AppUser) session.getAttribute("user");
    List<Recipe> recipes = null;
    if (request.getAttribute("recipes") != null) {
        recipes = (List<Recipe>) request.getAttribute("recipes");
    }
%>
<%@ include file="/includes/header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cabinet</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="container">
    <h2>Welcome, <%= user.getUsername() %>!</h2>

    <div class="user-info">
        <p><strong>Email:</strong> <%= user.getEmail() %>
        </p>
        <p><strong>Username:</strong> <%= user.getUsername() %>
        </p>
        <p><strong>Role:</strong> <%= user.getRole() %>
        </p>
        <p><strong>Avatar</strong></p>
        <img style="max-width: 400px" src="<%= user.getImage()%>" alt="<%= user.getUsername()%>">
    </div>
    <a href="cabinet/edit" class="edit-button">Edit Profile</a>
    <div class="recipe-grid">
        <% if (recipes != null && !recipes.isEmpty()) { %>
        <% for (Recipe recipe : recipes) { %>
        <div class="recipe-card">
            <img src="<%= recipe.getTitleImage() %>" alt="<%= recipe.getTitle() %>">
            <h2><%= recipe.getTitle() %>
            </h2>
            <p><strong>Calories:</strong> <%= recipe.getCalories() %> kcal</p>
            <a href="recipes?id=<%= recipe.getId() %>" class="view-button">View Recipe</a>
            <a href="recipes/edit?id=<%= recipe.getId() %>" class="edit-button">Edit Recipe</a>
        </div>
        <% } %>
        <% } else { %>
        <p>No recipes found.</p>
        <% } %>
    </div>

    <div class="buttons">
        <form action="" method="post">
            <input type="hidden" name="id" value="<%= user.getId()%>">
            <button type="submit" class="btn delete">Delete Account</button>
        </form>
    </div>
</div>
</body>
</html>

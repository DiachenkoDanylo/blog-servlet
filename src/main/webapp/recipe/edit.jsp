<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.diachenko.dietblog.model.Recipe" %>
<%
    Recipe recipe = (Recipe) request.getAttribute("recipe");
%>
<%@ include file="/includes/header.jsp" %>
<html>
<head>
    <title>Edit a recipe</title>
</head>
<body>
<h2>Recipe editing form</h2>
<% if (request.getParameter("error") != null && request.getParameter("error").equals("The title was already in use")) { %>
<p style="color:red;">This title was already in user</p>
<p style="color: green">Please change title</p>
<% } %>
<form action="${pageContext.request.contextPath}/recipes/edit" method="POST" enctype="multipart/form-data">
    <p>Recipe ID: <%= recipe.getId() %>
    </p>
    <input type="hidden" name="id" value="<%= recipe.getId() %>">
    <label>Title: <input type="text" name="title" value="<%= recipe.getTitle() %>" required></label><br>

    <label>Description: <input type="text" name="description" value="<%= recipe.getDescription() %>"
                               required></label><br>

    <label>Calories: <input type="number" name="calories" value="<%= recipe.getCalories() %>" required></label><br>

    <label>Image: <input type="file" name="image" accept="image/*"></label><br>

    <%-- Display existing image if available --%>
    <% if (recipe.getTitleImage() != null && !recipe.getTitleImage().isEmpty()) { %>
    <p>Current Image:</p>
    <img style="max-width: 400px" src="/<%= recipe.getTitleImage() %>" alt="<%= recipe.getTitle() %>">
    <% } %>
    <button type="submit">Apply</button>
</form>
</body>
</html>

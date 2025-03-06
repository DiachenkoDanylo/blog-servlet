<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/header.jsp" %>
<html>

<head>
    <title>Create a recipe</title>
</head>
<body>
<h2>Recipe creating form</h2>
<% if (request.getParameter("error") != null && request.getParameter("error").equals("The title was already in use")) { %>
<p style="color:red;">This title was already in user</p>
<p style="color: green">Please change title</p>
<% } %>
<form action="${pageContext.request.contextPath}/recipes/create" method="POST" enctype="multipart/form-data">
    Title: <input type="text" name="title" placeholder="Recipe Title" required>
    Description: <input type="text" name="description" placeholder="Description" required>
    Calories: <input type="number" name="calories" placeholder="Calories" required>
    Image: <input type="file" name="image" accept="image/*" required>
    <button type="submit">Create Recipe</button>
</form>
</body>
</html>

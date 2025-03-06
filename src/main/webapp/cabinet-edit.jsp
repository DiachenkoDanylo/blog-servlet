<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.diachenko.dietblog.model.AppUser" %>
<%
    AppUser user = (AppUser) session.getAttribute("user");
%>
<%@ include file="/includes/header.jsp" %>
<html>
<head>
    <title>Edit a recipe</title>
</head>
<body>
<h2>Profile editing form</h2>
<% if (request.getParameter("error") != null && request.getParameter("error").equals("Server error")) { %>
<p style="color:red;">Username or email are already in user</p>
<p style="color: green">Please change and apply</p>
<% } %>
<form action="${pageContext.request.contextPath}/cabinet/edit" method="POST" enctype="multipart/form-data">
    <p>Profile ID: <%= user.getId() %>
    </p>
    <input type="hidden" name="id" value="<%= user.getId() %>">
    <label>Username: <input type="text" name="username" value="<%= user.getUsername() %>" required></label><br>
    <label>Email: <input type="text" name="email" value="<%= user.getEmail() %>" required></label><br>
    <label>Image: <input type="file" name="image" accept="image/*"></label><br>

    <%-- Display existing image if available --%>
    <% if (user.getImage() != null && !user.getImage().isEmpty()) { %>
    <p>Current Image:</p>
    <img style="max-width: 400px" src="/<%= user.getImage() %>" alt="<%= user.getUsername() %>">
    <% } %>
    <button type="submit">Apply</button>
</form>
</body>
</html>

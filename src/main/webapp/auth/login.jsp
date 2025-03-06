<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/includes/header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
<% if (request.getParameter("error") != null && request.getParameter("error").equals("Invalid credentials")) { %>
<p style="color:red;">Invalid credentials</p>
<% } %>
<div class="register-form">
    <form method="POST" action="login">
        Email: <input type="text" name="email" required><br>
        Password: <input type="password" name="password" required><br>
        <button type="submit">Login</button>
        <a href="${pageContext.request.contextPath}/register" class="view-button">Register</a>
    </form>
</div>
</body>
</html>

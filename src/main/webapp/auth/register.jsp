<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/header.jsp" %>
<html>
<head>
    <title>Registration Form</title>
</head>
<body>
<h2>Registration Form</h2>
<% if (request.getParameter("error") != null && request.getParameter("error").equals("The email was already in use")) { %>
<p style="color:red;">This email was already in user</p>
<p style="color: green">Please Log in or register with other email</p>
<% } %>
<div class="register-form">
    <form method="POST" action="register">
        Username: <input type="text" name="username" required><br>
        Password: <input type="password" name="password" required><br>
        Email: <input type="email" name="email" required><br>
        <button type="submit">Register</button>
    </form>
</div>
</body>
</html>

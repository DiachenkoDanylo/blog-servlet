<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.diachenko.dietblog.model.AppUser" %>
<%
    List<AppUser> appUserList = (List<AppUser>) request.getAttribute("authorsList");
%>
<%@ include file="/includes/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>All authors </title>
</head>
<body>
<div class="recipe-grid">
    <% if (appUserList != null && !appUserList.isEmpty()) { %>
    <% for (AppUser appUser : appUserList) { %>
    <div class="recipe-card">
        <img style="max-width: 400px" src="<%= appUser.getImage() %>" alt="<%= appUser.getUsername()%>">
        <h2><%= appUser.getUsername() %>
        </h2>
        <a href="authors?id=<%= appUser.getId() %>" class="view-button">View Author</a>
    </div>
    <% } %>
    <% } else { %>
    <p>No authors found.</p>
    <% } %>
</div>
</body>
</html>

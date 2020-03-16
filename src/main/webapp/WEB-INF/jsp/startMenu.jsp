<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<h2>Welcome, ${user_login}!</h2>
<form action="test" method="POST">
    <input type="hidden" name="command" value="sign_out"/>
    <button type="submit" class="btn btn-outline-primary btn-sm">Sign out</button>
</form>
</body>
</html>

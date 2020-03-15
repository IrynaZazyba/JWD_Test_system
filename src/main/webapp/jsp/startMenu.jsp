<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<h2>Welcome,${user_login}!</h2>
<form action="test" method="POST">
    <input type="hidden" name="command" value="sign_out"/>
    <input type="submit" value="Sign out"/>
</form>
</body>
</html>

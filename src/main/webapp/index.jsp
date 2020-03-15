<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<form action = "test" method = "POST">
    <input type="hidden" name="command" value="sign_up"/>
    Login: <input type = "text" name = "login" />
    <br />
    <br />
    Password: <input type = "text" name = "password" />
    <br />
    <br />
    First Name: <input type = "text" name = "first_name"/>
    <br />
    <br />
    Last Name: <input type = "text" name = "last_name" />
    <br />
    <br />
    <input type = "submit" value = "Submit" />
</form>
<form action = "test" method = "POST">
    <input type="hidden" name="command" value="sign_in"/>
    Login: <input type = "text" name = "login" />
    <br />
    <br />
    Password: <input type = "text" name = "password" />
    <br />
    <br />
    <input type = "submit" value = "Submit" />
</form>
</body>
</html>
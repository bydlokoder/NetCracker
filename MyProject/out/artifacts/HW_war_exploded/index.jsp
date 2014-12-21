<%--
  Created by IntelliJ IDEA.
  User: Амфетамин
  Date: 26.10.14
  Time: 0:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Sample.AuthData" %>
<%@ page import="utils.User" %>
<%@ page import="utils.Utils" %>
<%@ page import="java.util.HashMap" %>

<%-- инициализация --%>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Авторизация</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>


<form method="post" action="/auth.jsp" class="login">
    <p>
        <label for="email">Логин:</label>
        <input type="text" name="email" id="email" value="name@example.com">
    </p>

    <p>
        <label for="password">Пароль:</label>
        <input type="password" name="password" id="password" value="4815162342">
    </p>

    <p class="login-submit">
        <button type="submit" class="login-button">Войти</button>
    </p>

    <p class="register"><a href="register.jsp">Регистрация</a></p>

    <p class="register"><% String result = (String)request.getAttribute(Utils.Auth_Result);
        if (result != null) {
            out.print(result);
        }
    %></p>

</form>
</body>
</html>

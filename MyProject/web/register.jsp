<%--
  Created by IntelliJ IDEA.
  User: Амфетамин
  Date: 26.10.14
  Time: 0:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="utils.Utils" %>

<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Регистрация</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>


<form method="post" action="/register" class="login">
    <p>
        <label for="email">Логин:</label>
        <input type="text" name="email" id="email" value="name@example.com">
    </p>

    <p>
        <label for="password">Пароль:</label>
        <input type="password" name="password" id="password" value="4815162342">
    </p>

    <p class="login-submit">
        <button type="submit" class="login-button">Зарегестрироваться</button>
    </p>

    <p class="register"><a href="/index.jsp">Авторизация</a></p>

    <p class="register"><%
        String result = (String)request.getAttribute(Utils.Register_Result);
        if (result != null) {
            out.print(result);
        }
    %></p>

</form>
</body>
</html>

<%@ page import="utils.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html; charset=windows-1251" %>
<link rel="stylesheet" href="css/style.css">
<html>
<head>
    <title>
        ������� �������������
    </title>
</head>

<body>
<h1>������� � �������</h1>

<h2>�������� ������������</h2>
<%-- ����� ���������� ������ --%>
<FORM name="form1" method="post">
    <p>
        <label for="email">�����:</label>
        <input type="text" name="email" id="email" maxlength="30" value="name@example.com">
    </p>

    <p>
        <label for="password">������:</label>
        <input type="password" name="password" id="password" value="4815162342">
    </p>
    <p class="login-submit">
        <button type="submit" class="login-button">�����</button>
    </p>
</FORM>
<%
    String password = request.getParameter("password");
    String login = request.getParameter("email");
    String check = User.check(password, login);
    if (check == null) {
        boolean result = userSet.add(new User(password, login, new Date()));
        if (!result) {
            out.println("User with this email already exists");
        }
    } else {
        out.println(check);
    }
%>

<h2>������� � ��������������</h2>

<%-- ������������� --%>
<%! Set<User> userSet = new HashSet<User>(); %>


<table border="1px" cellpadding="8px">
    <%-- �������� ������� � ������� --%>
    <tr>
        <th>Login</th>
        <th>Password's Hash</th>
        <th>���� �����������</th>
    </tr>
    <%-- �������� ������ --%>
    <%
        Iterator iterator = userSet.iterator();
        while (iterator.hasNext()) {
            out.print(iterator.next());
        }
    %>
</table>
</body>
</html>
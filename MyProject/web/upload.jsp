<%@ page import="utils.Factory" %>
<%@ page import="utils.UserFile" %>
<%@ page import="utils.Utils" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Амфетамин
  Date: 26.10.14
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Загрузка файла</title>
    <link rel="stylesheet" href="css/upload.css">
</head>
<body>

<form method="post" action="/upload" enctype="multipart/form-data" style="text-align:center">
    <p><input type="file" name="file" accept=".xls,.xlsx">
        <input type="submit" value="Отправить"></p>

    <p class="register"><%
        String result = (String) request.getAttribute(Utils.Upload_Result);
        if (result != null) {
            out.print(result);
        }
    %></p>

    <%
        String userID = Utils.checkCache(request);
        if (!(userID == null || userID.isEmpty())) {
            long id = Long.decode(userID);

            List<UserFile> userFiles = Factory.getInstance().getFileDAO().getAllFiles(id);
            if (userFiles != null) {
                if (userFiles.size() > 0) {
                    out.println("<table cellspacing='0'>");
                    out.println("<tr>");
                    out.println("<th>Имя файла</th>");
                    out.println("<th>XML</th>");
                    out.println("<th>Исходный файл</th>");
                    out.println("</tr>");

                    for (UserFile file : userFiles) {
                        out.println("<tr>");
                        out.println("<td>" + file.getName() + "</td>");
                        out.println("<td>" + "<a href=\"/showxml.jsp?" + Utils.FILE_TO_SHOW + "=" + file.getId() + "\">" + "Посмотреть" + "</a><br/>" + "</td>");
                        out.println("<td>Скачать</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                } else {
                    out.println("<p style=\"text-align: center;\"> <span style=\"font-size:36px;\"><strong>You don&#39;t have any files</strong></span></p>");
                }

            }
        } else {
            request.setAttribute(Utils.Auth_Result, "Authentication error");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    %>

</form>
</body>
</html>

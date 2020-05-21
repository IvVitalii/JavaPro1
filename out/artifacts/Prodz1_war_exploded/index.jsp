<%@ page import="com.ivy.StatServlet" %>
<%--
  Created by IntelliJ IDEA.
  User: Vetal
  Date: 19.05.2020
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Prodz1</title>
    <style>
        h1.logged {
            text-decoration: underline;
        }

        td {
            border: 3px solid yellow
        }

        td.submit {
            text-align: center;
        }

        table {
            background-color: #0099ff;

        }

        h1.idea {
            color: green
        }

        h1.eclipse {
            color: blue
        }

        h1.netbeans {
            color: darkgoldenrod
        }
    </style>

</head>
<body>

<%String inBase = (String) session.getAttribute("inBase");%>
<%String sended = (String) session.getAttribute("sended");%>
<%String userName = (String) session.getAttribute("login");%>


<%if (inBase == null || inBase.equals("logout")) {%>

<form action="/statistic" method="POST">
    <table>
        <tr>
            <th>Login</th>
        </tr>
        <tr>
            <td><input type="text" name="login"></td>
        </tr>
        <tr>
            <th>Password</th>
        </tr>
        <tr>
            <td><input type="password" name="pass"></td>
        </tr>
        <tr>
            <td class="submit"><input type="submit" name="submit"></td>
        </tr>
    </table>
</form>
<%} %>
<%if (sended == null && inBase != null && inBase.equals("login")) {%>
<h1 class="logged">Logged as <%=userName%>
</h1>
<form action="/statistic" method="post">
    Do you like Idea?<br>
    <input type="radio" name="q1" value="yes">Yes<br>
    <input type="radio" name="q1" value="no"> No<br>
    <hr width="150px" align="left">
    Do you like Eclipse?<br>
    <input type="radio" name="q2" value="yes"> Yes<br>
    <input type="radio" name="q2" value="no">No<br>
    <hr width="150px" align="left">
    Do you like NetBeans?<br>
    <input type="radio" name="q3" value="yes">Yes <br>
    <input type="radio" name="q3" value="no">No<br>
    <hr width="150px" align="left">
    <input type="submit" name="send" value="send">
    <hr width="150px" align="left">
</form>
<form action="/statistic" method="get">
    <input type="submit" name="logout" value="logout">
</form>
<%}%>
<%if (sended != null && inBase.equals("login")) {%>

<h1 class="logged">Logged as <%=userName%>
</h1>
<h1 class="idea">Idea: like-<%=StatServlet.ide.get(StatServlet.Q1_YES)%> |
    dislike-<%=StatServlet.ide.get(StatServlet.Q1_NO)%>
</h1>
<h1 class="eclipse">Eclipse: like-<%=StatServlet.ide.get(StatServlet.Q2_YES)%> |
    dislike-<%=StatServlet.ide.get(StatServlet.Q2_NO)%>
</h1>
<h1 class="netbeans">NetBeans: like-<%=StatServlet.ide.get(StatServlet.Q3_YES)%>|
    dislike-<%=StatServlet.ide.get(StatServlet.Q3_NO)%>
</h1>
<form action="/statistic" method="get">
    <input type="submit" name="logout" value="logout">
</form>
<%}%>
</body>
</html>

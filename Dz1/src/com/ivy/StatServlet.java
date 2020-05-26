package com.ivy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicIntegerArray;

@WebServlet(name = "Servlet", urlPatterns = "/statistic")
public class StatServlet extends HttpServlet {
    public static HashMap<String, String> users = new HashMap<>();
    static int[] arr = new int[6];
    public static AtomicIntegerArray ide = new AtomicIntegerArray(arr);
    public static final int Q1_YES = 0;
    public static final int Q1_NO = 1;
    public static final int Q2_YES = 2;
    public static final int Q2_NO = 3;
    public static final int Q3_YES = 4;
    public static final int Q3_NO = 5;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String inBase = (String) session.getAttribute("inBase");
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        if (inBase == null || inBase.equals("logout")) {

            if (users.containsKey(login) && check(login, pass)) {
                session.setAttribute("inBase", "login");
                session.setAttribute("login", login);
            }
            if (login != null && !users.containsKey(login) && login.length() > 1) {
                session.setAttribute("login", login);
                session.setAttribute("password", pass);
                users.put(login, pass);
                session.setAttribute("inBase", "login");
                session.removeAttribute("sended");
            }
        }
        if (inBase != null && inBase.equals("login") && !users.containsKey(login)) {
            String q1 = request.getParameter("q1");
            String q2 = request.getParameter("q2");
            String q3 = request.getParameter("q3");
            if (q1 != null && q2 != null && q3 != null) {
                int answer1 = q1.equals("yes") ? Q1_YES : Q1_NO;
                ide.getAndIncrement(answer1);
                int answer2 = q2.equals("yes") ? Q2_YES : Q2_NO;
                ide.getAndIncrement(answer2);
                int answer3 = q3.equals("yes") ? Q3_YES : Q3_NO;
                ide.getAndIncrement(answer3);
                session.setAttribute("sended", "yes");
            }
        }


        response.sendRedirect("index.jsp");
    }

    public boolean check(String login, String pass) {
        if (users.get(login).equals(pass)) {
            return true;
        }
        return false;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("logout") != null) {
            session.setAttribute("inBase", "logout");
        }
        response.sendRedirect("index.jsp");
    }
}

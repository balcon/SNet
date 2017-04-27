package com.epam.study.snet.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/locale")
public class LocaleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("locale", req.getParameter("locale"));
        String currentURL = req.getParameter("currentPage");
        String queryString = req.getParameter("queryString");
        if(queryString!=null&&(!queryString.isEmpty())) currentURL+="?"+queryString;
        resp.sendRedirect(currentURL);
    }
}
package com.practice;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Hello from HelloServlet!</h2>");
        out.println("<p>This Java web application is deployed via Jenkins to Tomcat.</p>");
        out.println("</body></html>");
    }
}

package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/index.html");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = getDispatcherByAction(action, context);
        dispatcher.forward(req, resp);
    }

    private RequestDispatcher getDispatcherByAction(String action, ServletContext context) {
        RequestDispatcher dispatcher;
        switch (action) {
            case "add":
                dispatcher = context.getRequestDispatcher("/addUser.html");
                break;
            case "delete":
                dispatcher = context.getRequestDispatcher("/deleteUser.html");
                break;
            case "update":
                dispatcher = context.getRequestDispatcher("/updateAddress.html");
                break;
            default:
                throw new IllegalArgumentException("You entered wrong word!");
        }
        return dispatcher;
    }

}

package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.exception.UpdateUserException;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateAddressServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/updateAddress.html");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idString = req.getParameter("id");
        Integer id = Integer.parseInt(idString);
        String address = req.getParameter("address");
        if (address.equals("")) {
            throw new IllegalArgumentException("You should fill all fields.");
        }
        try {
            userService.updateAddress(id, address);
            printMessage(resp, "Updated successfully!");
        } catch (UpdateUserException e) {
            logger.error(e.getMessage(), e);
            printMessage(resp, e.getMessage());
        }
    }

    private void printMessage(HttpServletResponse resp, String message) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println(message);
            out.println("</body></html>");
        }
    }

}

package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.exception.FindAllUsersException;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PrintAllUsersServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<UserDTO> users = null;
        try {
            users = userService.findAll();
        } catch (FindAllUsersException e) {
            logger.error(e.getMessage(), e);
            printMessage(resp, e.getMessage());
        }
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            users.forEach(user -> out.println(user + "<br>"));
            out.println("</body></html>");
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

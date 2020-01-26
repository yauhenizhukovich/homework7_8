package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.exception.AddUserException;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddUserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/addUser.html");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String isActiveString = req.getParameter("is_active");
        String ageString = req.getParameter("age");
        String address = req.getParameter("address");
        String telephone = req.getParameter("telephone");
        List<String> parameters = new ArrayList<>();
        parameters.add(username);
        parameters.add(password);
        parameters.add(ageString);
        parameters.add(address);
        parameters.add(telephone);
        for (String parameter : parameters) {
            if (parameter.equals("")) {
                throw new IllegalArgumentException("You should fill all fields.");
            }
        }
        if (!isActiveString.equals("true") && !isActiveString.equals("false")) {
            throw new IllegalArgumentException("Wrong active value.");
        }
        try {
            boolean isActive = Boolean.parseBoolean(isActiveString);
            int age = Integer.parseInt(ageString);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setPassword(password);
            userDTO.setActive(isActive);
            userDTO.setAge(age);
            userDTO.setAddress(address);
            userDTO.setTelephone(telephone);
            UserDTO addedUserDTO = userService.add(userDTO);
            printAddedUser(resp, addedUserDTO);
        } catch (NumberFormatException | AddUserException e) {
            logger.error("Incorrect format", e);
            printMessage(resp, e.getMessage());
        }
    }

    private void printAddedUser(HttpServletResponse resp, UserDTO addedUserDTO) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("User successfully added:<br>");
            out.println(addedUserDTO);
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

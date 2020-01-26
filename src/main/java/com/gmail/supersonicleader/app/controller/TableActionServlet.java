package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.supersonicleader.app.service.TableService;
import com.gmail.supersonicleader.app.service.exception.CreateTableException;
import com.gmail.supersonicleader.app.service.exception.DeleteTableException;
import com.gmail.supersonicleader.app.service.impl.TableServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TableActionServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private TableService tableService = TableServiceImpl.getInstance();

    @Override
    public void init() {
        deleteTables();
        createTables();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/index.html");
        dispatcher.forward(req, resp);
    }

    private void createTables() {
        try {
            tableService.createAllTables();
        } catch (CreateTableException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void deleteTables() {
        try {
            tableService.deleteAllTables();
        } catch (DeleteTableException e) {
            logger.error(e.getMessage(), e);
        }
    }

}

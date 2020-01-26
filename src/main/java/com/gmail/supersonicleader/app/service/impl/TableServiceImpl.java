package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.TableRepository;
import com.gmail.supersonicleader.app.repository.enums.CreateActionEnum;
import com.gmail.supersonicleader.app.repository.enums.DropActionEnum;
import com.gmail.supersonicleader.app.repository.impl.ConnectionRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.TableRepositoryImpl;
import com.gmail.supersonicleader.app.service.TableService;
import com.gmail.supersonicleader.app.service.exception.CreateTableException;
import com.gmail.supersonicleader.app.service.exception.DeleteTableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TableServiceImpl implements TableService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static TableService instance;
    private ConnectionRepository connectionRepository;
    private TableRepository tableRepository;

    private TableServiceImpl(ConnectionRepository connectionRepository, TableRepository tableRepository) {
        this.connectionRepository = connectionRepository;
        this.tableRepository = tableRepository;
    }

    public static TableService getInstance() {
        if (instance == null) {
            instance = new TableServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    TableRepositoryImpl.getInstance()
            );
        }
        return instance;
    }

    @Override
    public void deleteAllTables() throws DeleteTableException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (DropActionEnum action : DropActionEnum.values()) {
                    tableRepository.executeQuery(connection, action.getQuery());
                }
                logger.info("All tables are deleted.");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DeleteTableException("Tables failed to delete!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createAllTables() throws CreateTableException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (CreateActionEnum action : CreateActionEnum.values()) {
                    tableRepository.executeQuery(connection, action.getQuery());
                }
                logger.info("All tables are created.");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new CreateTableException("Tables failed to create!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

}

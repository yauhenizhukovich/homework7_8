package com.gmail.supersonicleader.app.repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionRepository {

    Connection getConnection() throws SQLException, ClassNotFoundException;

}

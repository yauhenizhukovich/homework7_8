package com.gmail.supersonicleader.app.service;

import com.gmail.supersonicleader.app.service.exception.CreateTableException;
import com.gmail.supersonicleader.app.service.exception.DeleteTableException;

public interface TableService {

    void createAllTables() throws CreateTableException;

    void deleteAllTables() throws DeleteTableException;

}

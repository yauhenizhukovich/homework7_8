package com.gmail.supersonicleader.app.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import com.gmail.supersonicleader.app.repository.model.UserInformation;

public interface UserInformationRepository extends GeneralRepository<UserInformation> {

    void updateAddress(Connection connection, Serializable id, String address) throws SQLException;

}

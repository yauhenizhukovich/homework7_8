package com.gmail.supersonicleader.app.service;

import java.io.Serializable;
import java.util.List;

import com.gmail.supersonicleader.app.service.exception.AddUserException;
import com.gmail.supersonicleader.app.service.exception.DeleteUserException;
import com.gmail.supersonicleader.app.service.exception.FindAllUsersException;
import com.gmail.supersonicleader.app.service.exception.UpdateUserException;
import com.gmail.supersonicleader.app.service.model.UserDTO;

public interface UserService {

    UserDTO add(UserDTO userDTO) throws AddUserException;

    List<UserDTO> findAll() throws FindAllUsersException;

    void updateAddress(Serializable id, String address) throws UpdateUserException;

    void delete(Serializable id) throws DeleteUserException;

}

package com.gmail.supersonicleader.app.service.impl;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.UserInformationRepository;
import com.gmail.supersonicleader.app.repository.UserRepository;
import com.gmail.supersonicleader.app.repository.impl.ConnectionRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.UserInformationRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.UserRepositoryImpl;
import com.gmail.supersonicleader.app.repository.model.User;
import com.gmail.supersonicleader.app.repository.model.UserInformation;
import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.exception.AddUserException;
import com.gmail.supersonicleader.app.service.exception.DeleteUserException;
import com.gmail.supersonicleader.app.service.exception.FindAllUsersException;
import com.gmail.supersonicleader.app.service.exception.UpdateUserException;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserService instance;
    private ConnectionRepository connectionRepository;
    private UserRepository userRepository;
    private UserInformationRepository userInformationRepository;

    private UserServiceImpl(
            ConnectionRepository connectionRepository,
            UserRepository userRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserRepositoryImpl.getInstance(),
                    UserInformationRepositoryImpl.getInstance());
        }
        return instance;
    }

    @Override
    public UserDTO add(UserDTO userDTO) throws AddUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = convertDTOToDatabaseUser(userDTO);
                user = userRepository.add(connection, user);
                UserInformation userInformation = user.getUserInformation();
                userInformation.setId(user.getId());
                userInformationRepository.add(connection, userInformation);
                userDTO = convertDatabaseUserToDTO(user);
                connection.commit();
                return userDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new AddUserException("Failed to add user!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<UserDTO> findAll() throws FindAllUsersException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> users = userRepository.findAll(connection);
                List<UserDTO> usersDTO = convertDatabaseUserToDTO(users);
                connection.commit();
                return usersDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new FindAllUsersException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public void updateAddress(Serializable id, String address) throws UpdateUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userInformationRepository.updateAddress(connection, id, address);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UpdateUserException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Serializable id) throws DeleteUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userInformationRepository.delete(connection, id);
                userRepository.delete(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DeleteUserException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<UserDTO> convertDatabaseUserToDTO(List<User> users) {
        return users.stream().
                map(this::convertDatabaseUserToDTO).
                collect(Collectors.toList());
    }

    private UserDTO convertDatabaseUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setActive(user.isActive());
        userDTO.setAge(user.getAge());
        UserInformation userInformation = user.getUserInformation();
        userDTO.setAddress(userInformation.getAddress());
        userDTO.setTelephone(userInformation.getTelephone());
        return userDTO;
    }

    private User convertDTOToDatabaseUser(UserDTO userDTO) {
        UserInformation userInformation = new UserInformation();
        userInformation.setAddress(userDTO.getAddress());
        userInformation.setTelephone(userDTO.getTelephone());
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setActive(userDTO.isActive());
        user.setAge(userDTO.getAge());
        user.setUserInformation(userInformation);
        return user;
    }

}

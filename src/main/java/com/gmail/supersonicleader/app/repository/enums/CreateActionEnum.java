package com.gmail.supersonicleader.app.repository.enums;

public enum CreateActionEnum {

    CREATE_USER_TABLE(
            "CREATE TABLE user (" +
                    "id INT(11) PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(40) NOT NULL, " +
                    "password VARCHAR(40) NOT NULL, " +
                    "is_active TINYINT(1) NOT NULL DEFAULT FALSE, " +
                    "age INT(2) NOT NULL" +
                    ");"),
    CREATE_USER_INFORMATION_TABLE(
            "CREATE TABLE user_information (" +
                    "user_id INT(11) PRIMARY KEY, " +
                    "address VARCHAR(100) NOT NULL, " +
                    "telephone VARCHAR(40) NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES user (id)" +
                    ");");

    private final String query;

    CreateActionEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

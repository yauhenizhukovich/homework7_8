package com.gmail.supersonicleader.app.repository.enums;

public enum DropActionEnum {
    DROP_USER_INFORMATION_TABLE("DROP TABLE IF EXISTS user_information"),
    DROP_USER_TABLE("DROP TABLE IF EXISTS user");

    private final String query;

    DropActionEnum(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

package com.inventory;

public interface Constants {
    String API_PATH_INVENTORY = "/inventory";
    String API_PATH_INVENTORY_BY_ID = "/inventory/{id}";
    String API_PATH_PARAM_ID = "id";
    String API_QUERY_PARAM_OFFSET = "skip";
    String API_QUERY_PARAM_LIMIT = "limit";
    String API_JSON_RESPONSE = "application/json";

    int OFFSET_MINIMUM = 0;
    int LIMIT_MINIMUM = 0;
    int LIMIT_MAXIMUM = 50;
}

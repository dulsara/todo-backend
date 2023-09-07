package com.quanloop.todo.util;

import java.util.HashMap;
import java.util.Map;

public class GlobalConstant {

    public static final Map<String,Integer> STATUS_MAP = new HashMap<>();

    public static final int INITIAL_STATUS = 1;


    static {
        // Initialize the status map
        STATUS_MAP.put( "NEW",1);
        STATUS_MAP.put( "IN_PROGRESS",2);
        STATUS_MAP.put("CANCELLED",3);
        STATUS_MAP.put("DONE",4);
    }
    public static class TodoValidateErrors {
        public static final String STATUS_IS_NOT_VALID = "Status is not valid";
        public static final String TODO_NOT_FOUND = "Todo not found with ID: ";
        public static final String TODO_ID_IS_MANDATORY = "Todo id must mandatory";
        public static final String TODO_TITLE_IS_NOT_EMPTY = "Title should not be EMPTY for Todo Create/Update Operation";
        public static final String TODO_DESCRIPTION_IS_NOT_EMPTY = "Description should not be EMPTY for Todo Create/Update Operation";
        public static final String TODO_STATUS_IS_NOT_EMPTY = "Status should not be EMPTY for Todo Create/Update Operation";
        public static final String STATUS_CAN_NOT_CHANGE = "TODO status can not be changed ";
        public static final String INITIAL_STATUS_NOT_VALID = "TODO Initial status can not be ";

    }
}

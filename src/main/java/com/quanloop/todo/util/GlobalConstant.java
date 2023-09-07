package com.quanloop.todo.util;

public class GlobalConstant {

    public enum TodoStatus {
        NEW, IN_PROGRESS, DONE, CANCELLED
    }

    public static class TodoValidateErrors {
        public static final String STATUS_IS_NOT_VALID = "Status is not valid";
        public static final String TODO_NOT_FOUND = "Todo not found with ID: ";
        public static final String TODO_ID_IS_MANDATORY = "Todo id must mandatory";
        public static final String TODO_TITLE_IS_NOT_EMPTY = "Title should not be EMPTY for Todo Create/Update Operation";
        public static final String TODO_DESCRIPTION_IS_NOT_EMPTY = "Description should not be EMPTY for Todo Create/Update Operation";
        public static final String TODO_STATUS_IS_NOT_EMPTY = "Status should not be EMPTY for Todo Create/Update Operation";

    }
}

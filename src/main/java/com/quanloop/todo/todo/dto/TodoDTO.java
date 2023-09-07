package com.quanloop.todo.todo.dto;


import com.quanloop.todo.validation.CreateValidation;
import com.quanloop.todo.validation.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoDTO {

    @NotEmpty(groups = {UpdateValidation.class,CreateValidation.class}, message = TODO_TITLE_IS_NOT_EMPTY)
    private String title;
    @NotEmpty(groups = {UpdateValidation.class,CreateValidation.class}, message = TODO_STATUS_IS_NOT_EMPTY)
    private String status;
    @NotEmpty(groups = {UpdateValidation.class,CreateValidation.class}, message = TODO_DESCRIPTION_IS_NOT_EMPTY)
    private String description;
    //ignore checking the null for create operation
    @Null(groups = CreateValidation.class)
    @NotNull(groups = UpdateValidation.class,message = TODO_ID_IS_MANDATORY)
    private Long id;
}

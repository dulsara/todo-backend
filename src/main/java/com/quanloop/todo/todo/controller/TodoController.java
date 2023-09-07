package com.quanloop.todo.todo.controller;

import com.quanloop.todo.todo.dto.TodoDTO;
import com.quanloop.todo.todo.model.Todo;
import com.quanloop.todo.todo.service.TodoService;
import com.quanloop.todo.validation.CreateValidation;
import com.quanloop.todo.validation.UpdateValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.TODO_ID_IS_MANDATORY;

@CrossOrigin
@RestController
@RequestMapping("api/todos")
@RequiredArgsConstructor

public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    public Todo createTodo(@Validated(CreateValidation.class) @RequestBody TodoDTO todoDTO) {
        return todoService.createTodo(todoDTO);
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable @NotNull(message = TODO_ID_IS_MANDATORY) Long id) {
        return todoService.getTodoById(id);
    }

    @PutMapping()
    public Todo updateTodo(@Validated(UpdateValidation.class) @RequestBody TodoDTO todoDTO) {
        return todoService.updateTodo(todoDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable @NotNull(message = TODO_ID_IS_MANDATORY)Long id) {
        todoService.deleteTodo(id);
    }
}

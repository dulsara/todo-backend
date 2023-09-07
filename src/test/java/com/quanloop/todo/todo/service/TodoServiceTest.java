package com.quanloop.todo.todo.service;

import com.quanloop.todo.todo.dto.TodoDTO;
import com.quanloop.todo.todo.model.Todo;
import com.quanloop.todo.todo.repository.TodoRepository;
import com.quanloop.todo.util.GlobalConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;
    @Test
    void successful_todo_list_fetch_should_not_throw_any_exception() {
        String title = "test_title";
        String description = "test_description";
        String status = "NEW";
        Long id = 1L;

        Todo todo = getTodoObject(title,description,status,id);
        when(todoRepository.findAll()).thenReturn(List.of(todo));
        List<Todo> allTodos = todoService.getAllTodos();
        Assertions.assertNotNull(allTodos);
        Assertions.assertEquals(todo.getId(), allTodos.get(0).getId());
    }

    @Test
    void successful_todo_creation_should_not_throw_any_exception() {
        String title = "test_title";
        String description = "test_description";
        String status = "NEW";
        Long id = 1L;

        TodoDTO todoDTORequest = getTodoDTOObject(title,description,status,null);
        Todo todo = getTodoObject(title,description,status,id);
        when(todoRepository.save(any())).thenReturn(todo);
        Todo createdTodo = todoService.createTodo(todoDTORequest);
        Assertions.assertNotNull(createdTodo);
        Assertions.assertEquals(todo.getTitle(), createdTodo.getTitle());
    }

    @Test
    void unsuccessful_todo_creation_should_throw_exception_due_to_invalid_status() {
        String title = "test_title";
        String description = "test_description";
        String status = "NOT_VALID";

        TodoDTO todoDTORequest = getTodoDTOObject(title,description,status,null);
        Exception exception = assertThrows(Exception.class, () -> {
            Todo createdTodo = todoService.createTodo(todoDTORequest);
        });
        String expectedMessage = GlobalConstant.TodoValidateErrors.STATUS_IS_NOT_VALID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void successful_todo_fetch_should_not_throw_exception() {
        String title = "test_title";
        String description = "test_description";
        String status = "NEW";
        Long id = 1L;

        Todo todo = getTodoObject(title,description,status,id);
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        Todo fetchedTodo = todoService.getTodoById(id);
        Assertions.assertNotNull(fetchedTodo);
        Assertions.assertEquals(todo.getId(), fetchedTodo.getId());
    }

    @Test
    void unsuccessful_todo_fetch_should_throw_exception_due_to_non_existing_id() {
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(Exception.class, () -> {
            Todo fetchedTodo = todoService.getTodoById(id);
        });
        String expectedMessage = GlobalConstant.TodoValidateErrors.TODO_NOT_FOUND + id;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void successful_todo_update_should_not_throw_exception() {
        String title = "test_title";
        String description = "test_description";
        String oldStatus = "NEW";
        String newStatus = "IN_PROGRESS";
        Long id = 1L;

        TodoDTO todoDTORequest = getTodoDTOObject(title,description,newStatus,id);
        Todo existingTodo = getTodoObject(title,description,oldStatus,id);
        Todo updatedTodo = getTodoObject(title,description,newStatus,id);
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(updatedTodo)).thenReturn(updatedTodo);
        Todo latestUpdatedTodo = todoService.updateTodo(todoDTORequest);
        Assertions.assertNotNull(latestUpdatedTodo);
        Assertions.assertEquals(latestUpdatedTodo.getStatus(), newStatus);
    }

    @Test
    void unsuccessful_todo_update_should_throw_exception_due_to_non_existing_id() {
        String title = "test_title";
        String description = "test_description";
        String newStatus = "IN_PROGRESS";
        Long id = 1L;

        TodoDTO todoDTORequest = getTodoDTOObject(title,description,newStatus,id);
        when(todoRepository.findById(id)).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(Exception.class, () -> {
            Todo latestUpdatedTodo = todoService.updateTodo(todoDTORequest);
        });
        String expectedMessage = GlobalConstant.TodoValidateErrors.TODO_NOT_FOUND + id;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void unsuccessful_todo_update_should_throw_exception_due_to_invalid_todo_status() {
        String title = "test_title";
        String description = "test_description";
        String newStatus = "IN_VALID";
        Long id = 1L;

        TodoDTO todoDTORequest = getTodoDTOObject(title,description,newStatus,id);
        Exception exception = assertThrows(Exception.class, () -> {
            Todo latestUpdatedTodo = todoService.updateTodo(todoDTORequest);
        });
        String expectedMessage = GlobalConstant.TodoValidateErrors.STATUS_IS_NOT_VALID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private TodoDTO getTodoDTOObject(String title, String description, String status, Long id) {
        return TodoDTO.builder()
                .title(title)
                .description(description)
                .status(status)
                .id(id)
                .build();
    }

    private Todo getTodoObject(String title, String description, String status, Long id) {
        return Todo.builder()
                .title(title)
                .description(description)
                .status(status)
                .id(id)
                .build();
    }
}
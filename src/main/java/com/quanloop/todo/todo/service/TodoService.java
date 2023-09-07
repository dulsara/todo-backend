package com.quanloop.todo.todo.service;

import com.quanloop.todo.exception.exceptionType.BadClientException;
import com.quanloop.todo.todo.dto.TodoDTO;
import com.quanloop.todo.todo.model.Todo;
import com.quanloop.todo.todo.repository.TodoRepository;
import com.quanloop.todo.util.GlobalConstant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.STATUS_IS_NOT_VALID;
import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.TODO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper mapper = new ModelMapper();

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Transactional
    public Todo createTodo(TodoDTO todoDTO) {
        Todo newTodo = this.mapper.map(todoDTO, Todo.class);
        validateTodoRequest(newTodo);
        return todoRepository.save(newTodo);
    }

    public Todo getTodoById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            return todo.get();
        } else {
            throw new BadClientException(TODO_NOT_FOUND + id);
        }
    }

    @Transactional
    public Todo updateTodo(TodoDTO updatedTodoDto) {
        Todo updatedTodo = this.mapper.map(updatedTodoDto, Todo.class);
        validateTodoRequest(updatedTodo);
        Todo existingTodo = getTodoById(updatedTodo.getId());
        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setStatus(updatedTodo.getStatus());

        return todoRepository.save(existingTodo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    private void validateTodoRequest(Todo todo) throws BadClientException {
        validateTodoStatus(todo.getStatus());
    }

    private void validateTodoStatus(String value) {
        for (GlobalConstant.TodoStatus status : GlobalConstant.TodoStatus.values()) {
            if (status.name().equals(value)) {
                return;
            }
        }
        throw new BadClientException(STATUS_IS_NOT_VALID);
    }
}

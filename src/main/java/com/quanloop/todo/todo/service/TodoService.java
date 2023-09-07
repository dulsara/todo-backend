package com.quanloop.todo.todo.service;

import com.quanloop.todo.exception.exceptionType.BadClientException;
import com.quanloop.todo.todo.dto.TodoDTO;
import com.quanloop.todo.todo.model.Todo;
import com.quanloop.todo.todo.repository.TodoRepository;
import com.quanloop.todo.util.GlobalConstant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.quanloop.todo.util.GlobalConstant.INITIAL_STATUS;
import static com.quanloop.todo.util.GlobalConstant.STATUS_MAP;
import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.*;

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
        validateCreateTodoRequest(newTodo);
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
        Todo existingTodo = getTodoById(updatedTodo.getId());
        validateUpdateTodoRequest(updatedTodo,existingTodo);
        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setStatus(updatedTodo.getStatus());

        return todoRepository.save(existingTodo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    private void validateCreateTodoRequest(Todo todo) throws BadClientException {
        validateTodoStatus(todo.getStatus());
        validateInitialStatus(todo.getStatus());
    }

    private void validateUpdateTodoRequest(Todo todo,Todo existingTodo) throws BadClientException {
        validateTodoStatus(todo.getStatus());
        validateStatusSequence(todo.getStatus(),existingTodo.getStatus());
    }

    private void validateTodoStatus(String value) {
        if (!STATUS_MAP.containsKey(value)) {
            throw new BadClientException(STATUS_IS_NOT_VALID);
        }
    }
    private void validateStatusSequence(String newStatus, String oldStatus) {
        if (!(STATUS_MAP.get(newStatus) >= STATUS_MAP.get(oldStatus))) {
            throw new BadClientException(STATUS_CAN_NOT_CHANGE + " from " + oldStatus + " to " + newStatus);
        }
    }

    private void validateInitialStatus(String newStatus) {
        if ((STATUS_MAP.get(newStatus) > INITIAL_STATUS)) {
            throw new BadClientException(INITIAL_STATUS_NOT_VALID + newStatus );
        }
    }
}

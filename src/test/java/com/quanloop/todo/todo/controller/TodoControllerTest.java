package com.quanloop.todo.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanloop.todo.config.user.model.Role;
import com.quanloop.todo.config.user.model.User;
import com.quanloop.todo.todo.dto.TodoDTO;
import com.quanloop.todo.todo.repository.TodoRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Set;

import static com.quanloop.todo.util.GlobalConstant.TodoValidateErrors.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SqlGroup({
        @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
})
class TodoControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        User user = new User();
        user.setUserName("test-user");
        user.setPassword("test@123");
        user.setId(1L);
        user.setRoles(Set.of(new Role("ROLE_USER")));
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @SneakyThrows
    @Test
    void successful_todo_list_fetch_all_operation() {

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @SneakyThrows
    @Test
    void successful_todo_item_create_operation() {

        String title = "test_title";
        String description = "test_description";
        String status = "NEW";

        TodoDTO todoCreationRequest = getTodoDTOObject(title, description, status, null);
        String todoCreationRequestString = objectMapper.writeValueAsString(todoCreationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoCreationRequestString))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));

        Assertions.assertEquals(5, todoRepository.findAll().size());
    }

    @SneakyThrows
    @Test
    void unsuccessful_todo_item_create_operation_due_to_invalid_status() {

        String title = "test_title";
        String description = "test_description";
        String status = "INVALID";

        TodoDTO todoCreationRequest = getTodoDTOObject(title, description, status, null);
        String todoCreationRequestString = objectMapper.writeValueAsString(todoCreationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoCreationRequestString))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(STATUS_IS_NOT_VALID));
    }

    @SneakyThrows
    @Test
    void unsuccessful_todo_item_create_operation_due_to_null_title() {

        String description = "test_description";
        String status = "NEW";

        TodoDTO todoCreationRequest = getTodoDTOObject("", description, status, null);
        String todoCreationRequestString = objectMapper.writeValueAsString(todoCreationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoCreationRequestString))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TODO_TITLE_IS_NOT_EMPTY));
    }

    @SneakyThrows
    @Test
    void successful_todo_item_fetch_operation() {

        Long fetchedTodoId = 100000L;
        mockMvc.perform(get("/api/todos/{id}", fetchedTodoId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(fetchedTodoId.toString()));
    }

    @SneakyThrows
    @Test
    void unsuccessful_todo_item_fetch_operation_for_non_existing_id() {

        Long fetchedTodoId = 1000000000L;
        mockMvc.perform(get("/api/todos/{id}", fetchedTodoId))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TODO_NOT_FOUND + fetchedTodoId));
    }

    @SneakyThrows
    @Test
    void successful_todo_item_update_operation() {

        String title = "DEVELOPMENT 1 UPDATE";
        String description = "test_description update";
        String status = "IN_PROGRESS";
        Long updatableTodoId = 100000L;

        TodoDTO todoUpdateRequest = getTodoDTOObject(title, description, status, updatableTodoId);
        String todoUpdateRequestString = objectMapper.writeValueAsString(todoUpdateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoUpdateRequestString))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status));

        Assertions.assertEquals(title, todoRepository.findById(updatableTodoId).get().getTitle());
    }

    private TodoDTO getTodoDTOObject(String title, String description, String status, Long id) {
        return TodoDTO.builder()
                .title(title)
                .description(description)
                .status(status)
                .id(id)
                .build();
    }
}
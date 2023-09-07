package com.quanloop.todo.config.data;


import com.quanloop.todo.config.user.repository.RoleRepository;
import com.quanloop.todo.config.user.repository.UserRoleRepository;
import com.quanloop.todo.config.user.model.Role;
import com.quanloop.todo.config.user.model.User;
import com.quanloop.todo.config.user.model.UserRole;
import com.quanloop.todo.config.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;



    @Override
    public void run(ApplicationArguments args) throws InterruptedException {
        var quanloop = new User(1L, "quanloop","quanloop@123");
        var role = new Role(2L, "ROLE_USER");
        var userRole = new UserRole(1L, 2L);
        userService.save(quanloop);
        roleRepository.save(role);
        userRoleRepository.save(userRole);

    }

}

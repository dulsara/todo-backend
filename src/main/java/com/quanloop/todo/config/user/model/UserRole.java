package com.quanloop.todo.config.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
@Data
@IdClass(UserRoleId.class)
public class UserRole {

    @Id
    private Long user_id;

    @Id
    private Long role_id;

}

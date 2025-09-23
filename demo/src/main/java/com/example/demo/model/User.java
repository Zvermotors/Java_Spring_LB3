package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id//первичный ключ таблицы
    @GeneratedValue(strategy = GenerationType.IDENTITY)//указывает, что значение для этого
    // ключа будет генерироваться базой данных автоматически при вставке новой записи.
    private Long id;

    @Column(unique = true, nullable = false)//  @Column служит для определения свойств столбца в таблице базы данных
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;
//`fetch = FetchType.EAGER` — означает, что при загрузке пользователя все его роли подтянутся сразу (жадная загрузка).
    @ManyToMany(fetch = FetchType.EAGER) // один пользователь может иметь много ролей, а одна роль — принадлежать множеству пользователей.
    @JoinTable(//описание вспомогательной таблицы
            name = "user_roles",//название таблицы, которая связывает пользователей и роли.
            joinColumns = @JoinColumn(name = "user_id"),//колонка в таблице `user_roles`, которая ссылается на пользователя
            inverseJoinColumns = @JoinColumn(name = "role_id")//колонка, которая ссылается на роль.
    )
    private Set<Role> roles = new HashSet<>();

    // Конструкторы, геттеры, сеттеры
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
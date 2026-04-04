package userprofile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
@NotBlank(message = "Username is required")
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
@Column(unique = true, nullable = false)
private String username;

@Email(message = "Email must be valid")
@NotBlank(message = "Email is required")
@Column(unique = true, nullable = false)
private String email;

public User() {}
    public User(String username, String email) {
    this.username = username;
    this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {
    return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
    return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
}}

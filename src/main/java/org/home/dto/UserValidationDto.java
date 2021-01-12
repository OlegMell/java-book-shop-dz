package org.home.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class UserValidationDto {
    private String id;

    @NotBlank(message = "Username is required field!")
    private String username;

    @NotBlank(message = "Password is required field!")
    @Length(min = 4, max = 8)
    private String password;

    @NotBlank(message = "Confirm password is required field!")
    @Length(min = 4, max = 8)
    private String confirmPassword;

    @NotBlank(message = "Email is required field!")
    private String email;

    public UserValidationDto(@NotBlank(message = "Username is required field!") String username,
                             @NotBlank(message = "Password is required field!") String password,
                             @NotBlank(message = "Confirm password is required field!") String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

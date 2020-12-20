package org.home.dto;

import javax.validation.constraints.NotBlank;
import java.beans.Transient;

public class UserValidationDto {
    private Long id;

    @NotBlank(message = "Username is required field!")
    private String username;

    @NotBlank(message = "Password is required field!")
    private String password;

    @NotBlank(message = "Confirm password is required field!")
    private String confirmPassword;

    public UserValidationDto(@NotBlank(message = "Username is required field!") String username,
                             @NotBlank(message = "Password is required field!") String password,
                             @NotBlank(message = "Confirm password is required field!") String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}

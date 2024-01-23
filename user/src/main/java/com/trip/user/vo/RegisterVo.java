package com.trip.user.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterVo {

    @NotBlank(message = "Please provide username")
    @Length(min=6, max=15, message = "Username should be 6-15 characters long")
    private String username;

    @NotBlank(message = "Please provide password")
    @Length(min=6, max=15, message = "Password should be 6-15 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@.#$!%*?&^]).{6,15}$",
            message = "Password should contain at least one uppercase letter, one lowercase letter, one number and one special symbol")
    private String password;
}

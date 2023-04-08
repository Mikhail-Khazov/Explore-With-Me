package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @Email
    @NotBlank(message = "Email is required field")
    @Size(min = 6, max = 320)
    private String email;
    @NotBlank(message = "Name is required field")
    @Size(min = 2, max = 128)
    private String name;
}

package pl.grabowski.usapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Getter
@RequiredArgsConstructor
public class CreateTeacherRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(min=2)
    private final String firstName;

    @NotBlank(message = "Last name is mandatory")
    private final String lastName;

    @NotNull(message = "Age is mandatory")
    @Min(18)
    private final int age;

    @NotBlank(message = "E-mail is mandatory")
    @Email
    private final String mail;

    @NotBlank(message = "Subject is mandatory")
    private final String subject;
}

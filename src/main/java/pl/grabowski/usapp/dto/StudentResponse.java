package pl.grabowski.usapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class StudentResponse {
    private final Long id;

    private final String firstName;

    private final String lastName;

    private final int age;

    private final String mail;

    private final String fieldOfStudy;
}

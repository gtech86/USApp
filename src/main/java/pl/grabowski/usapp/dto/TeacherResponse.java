package pl.grabowski.usapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TeacherResponse {
    private final Long id;

    private final String firstName;

    private final String lastName;

    private final int age;

    private final String mail;

    private final String subject;
}

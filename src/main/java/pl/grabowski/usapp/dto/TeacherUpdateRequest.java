package pl.grabowski.usapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class TeacherUpdateRequest {

    private final String firstName;

    private final String lastName;

    private final int age;

    private final String mail;

    private final String fieldOfStudy;
}

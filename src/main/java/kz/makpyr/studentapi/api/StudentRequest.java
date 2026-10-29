package kz.makpyr.studentapi.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
		@NotBlank String name,
		@NotBlank String surname,
		@NotBlank @Email String email,
		@NotBlank String studyGroup) {
}

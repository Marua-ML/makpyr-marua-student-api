package kz.makpyr.studentapi.service;

public class StudentNotFoundException extends RuntimeException {

	public StudentNotFoundException(Long id) {
		super("No student with id " + id);
	}

}

package kz.makpyr.studentapi.service;

public class DuplicateEmailException extends RuntimeException {

	public DuplicateEmailException(String email) {
		super("Email " + email + " is already used by another student");
	}

}

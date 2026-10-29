package kz.makpyr.studentapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "makpyr_marua_students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "study_group", nullable = false)
	private String studyGroup;

	protected Student() {
	}

	public Student(String name, String surname, String email, String studyGroup) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.studyGroup = studyGroup;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStudyGroup() {
		return studyGroup;
	}

	public void setStudyGroup(String studyGroup) {
		this.studyGroup = studyGroup;
	}

}

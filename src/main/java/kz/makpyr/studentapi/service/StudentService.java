package kz.makpyr.studentapi.service;

import java.util.List;

import kz.makpyr.studentapi.api.StudentRequest;
import kz.makpyr.studentapi.domain.Student;
import kz.makpyr.studentapi.domain.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {

	private final StudentRepository students;

	public StudentService(StudentRepository students) {
		this.students = students;
	}

	@Transactional(readOnly = true)
	public List<Student> findAll() {
		return students.findAll();
	}

	@Transactional(readOnly = true)
	public Student findById(Long id) {
		return students.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
	}

	public Student add(StudentRequest request) {
		Student student = new Student(request.name(), request.surname(), request.email(), request.studyGroup());
		return students.save(student);
	}

	public Student change(Long id, StudentRequest request) {
		Student student = findById(id);
		student.setName(request.name());
		student.setSurname(request.surname());
		student.setEmail(request.email());
		student.setStudyGroup(request.studyGroup());
		return student;
	}

	public void remove(Long id) {
		Student student = findById(id);
		students.delete(student);
	}

}

package kz.makpyr.studentapi.api;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import kz.makpyr.studentapi.domain.Student;
import kz.makpyr.studentapi.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/makpyr-marua/students")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}

	@GetMapping
	public List<Student> all() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Student one(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public ResponseEntity<Student> create(@Valid @RequestBody StudentRequest request) {
		Student created = service.add(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(created.getId())
			.toUri();
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public Student update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
		return service.change(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	}

}

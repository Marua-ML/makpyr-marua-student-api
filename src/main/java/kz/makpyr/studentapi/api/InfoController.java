package kz.makpyr.studentapi.api;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

	private final String fullName;

	private final String group;

	private final String studentId;

	private final ObjectProvider<BuildProperties> build;

	public InfoController(@Value("${student.full-name}") String fullName, @Value("${student.group}") String group,
			@Value("${student.id}") String studentId, ObjectProvider<BuildProperties> build) {
		this.fullName = fullName;
		this.group = group;
		this.studentId = studentId;
		this.build = build;
	}

	@GetMapping("/api/makpyr-marua/info")
	public InfoResponse info() {
		BuildProperties props = build.getIfAvailable();
		String project = (props != null) ? props.getName() : "makpyr-marua-student-api";
		String version = (props != null) ? props.getVersion() : "dev";
		return new InfoResponse(project, fullName, group, studentId, version);
	}

}

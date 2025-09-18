package pl.school.librus;

import org.springframework.boot.SpringApplication;

public class TestLibrusApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibrusApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

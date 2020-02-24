package cz.interview.exam.check.packager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PackagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(PackagerApplication.class, args);
		// We need to manually close spring boot app context because of scheduler
		run.close();
	}

}

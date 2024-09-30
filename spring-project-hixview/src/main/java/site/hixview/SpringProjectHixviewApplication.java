package site.hixview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringProjectHixviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectHixviewApplication.class, args);
	}

}

package site.hixview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringSideProject1BuildApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSideProject1BuildApplication.class, args);
	}

}

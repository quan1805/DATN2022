package huce.it.datnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DatnBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatnBackendApplication.class, args);
	}

	@GetMapping("/home")
	public String Homepage(){
		return "/homepage/home";
	}
//
//	@GetMapping
//	public String Hello() {
//		return "DATN2022";
//	}
}

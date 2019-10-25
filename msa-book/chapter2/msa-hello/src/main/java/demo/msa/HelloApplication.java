package demo.msa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin
@RestController
@SpringBootApplication
public class HelloApplication {

  @RequestMapping(method = RequestMethod.GET, path = "/hello")
  public String hello() {
    return "Hello";
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloApplication.class, args);
  }
}

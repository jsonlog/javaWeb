package demo.msa.zipkin.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @GetMapping("/hello")
  public String hello() {
    String name = jdbcTemplate.queryForObject(
        "SELECT name FROM customer WHERE id = 1",
        String.class
    );
    return "hello " + name;
  }
}

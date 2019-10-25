package demo.msa.zipkin.foo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class FooController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/foo")
  public String foo() {
    try {
      Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return restTemplate.getForObject("http://localhost:8082/bar", String.class);
  }
}

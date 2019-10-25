package demo.msa.zipkin.bar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class BarController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/bar")
  public String bar() {
    try {
      Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return restTemplate.getForObject("http://localhost:8080/hello", String.class);
  }
}

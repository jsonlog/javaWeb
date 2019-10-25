package demo.msa.product.server;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SayController {

  @GetMapping("/say1")
  public String say1(@RequestParam("name") String name) {
    return "hello " + name;
  }

  @GetMapping("/say2/{name}")
  public String say2(@PathVariable("name") String name) {
    return "hello " + name;
  }

  @GetMapping("/say3")
  public String say3(@RequestHeader("name") String name) {
    return "hello " + name;
  }

  @GetMapping("/list")
  public List<String> list() {
    List<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    return list;
  }

  @GetMapping("/map")
  public Map<String, Object> map() {
    Map<String, Object> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    return map;
  }

  @GetMapping("/bean")
  public Bean bean() {
    Bean bean = new Bean();
    bean.setFoo("a");
    bean.setBar(1);
    return bean;
  }
}

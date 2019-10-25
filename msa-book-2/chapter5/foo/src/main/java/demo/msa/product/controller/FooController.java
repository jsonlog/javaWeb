package demo.msa.product.controller;

import demo.msa.product.service.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class FooController {

  private FooService fooService;

  @Autowired
  public FooController(FooService fooService) {
    this.fooService = fooService;
  }

  @GetMapping("/foo")
  public void foo(@PathParam("name") String name) {
    fooService.insertFoo(name);
  }
}

package demo.msa.retrofit.hello.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class HelloClient {

  private HelloService helloService;

  @Autowired
  public HelloClient(Retrofit retrofit) {
    helloService = retrofit.create(HelloService.class);
  }

  public String say1(String name) throws IOException {
    return helloService.say1(name).execute().body();
  }

  public String say2(String name) throws IOException {
    return helloService.say2(name).execute().body();
  }

  public String say3(String name) throws IOException {
    return helloService.say3(name).execute().body();
  }

  public List<String> list() throws IOException {
    return helloService.list().execute().body();
  }

  public Map<String, Object> map() throws IOException {
    return helloService.map().execute().body();
  }

  public Bean bean() throws IOException {
    return helloService.bean().execute().body();
  }
}

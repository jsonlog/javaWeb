package demo.msa.okhttp.hello.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HelloClient {

  @Autowired
  private OkHttpClient okHttpClient;

  @Value("${client.root-uri}")
  private String rootUri;

  public String say1(String name) throws IOException {
    Request request = new Request.Builder()
        .get()
        .url(String.format("%s/say1?name=%s", rootUri, name))
        .build();
    Response response = okHttpClient.newCall(request).execute();
    return response.body().string();
  }

  public String say2(String name) throws IOException {
    Request request = new Request.Builder()
        .get()
        .url(String.format("%s/say2/%s", rootUri, name))
        .build();
    Response response = okHttpClient.newCall(request).execute();
    return response.body().string();
  }

  public String say3(String name) throws IOException {
    Request request = new Request.Builder()
        .get()
        .url(String.format("%s/say3", rootUri))
        .addHeader("name", name)
        .build();
    Response response = okHttpClient.newCall(request).execute();
    return response.body().string();
  }
}

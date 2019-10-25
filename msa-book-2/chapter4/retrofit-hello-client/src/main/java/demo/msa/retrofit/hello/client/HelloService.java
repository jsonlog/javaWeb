package demo.msa.retrofit.hello.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface HelloService {

  @GET("/say1")
  Call<String> say1(@Query("name") String name);

  @GET("/say2/{name}")
  Call<String> say2(@Path("name") String name);

  @GET("/say3")
  Call<String> say3(@Header("name") String name);

  @GET("/list")
  Call<List<String>> list();

  @GET("/map")
  Call<Map<String, Object>> map();

  @GET("/bean")
  Call<Bean> bean();
}

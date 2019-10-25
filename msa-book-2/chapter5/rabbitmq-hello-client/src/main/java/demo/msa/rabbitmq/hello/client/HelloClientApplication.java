package demo.msa.rabbitmq.hello.client;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class HelloClientApplication {

  @Autowired
  private HelloClient helloClient;

  @Bean
  public Queue helloQueue() {
    return new Queue("hello-queue");
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @PostConstruct
  public void init() {
//    helloClient.send("hello world");

//    Pojo pojo = new Pojo();
//    pojo.setFoo("a");
//    pojo.setBar(1);
//    helloClient.send(pojo);

    StopWatch watch = new StopWatch();
    watch.start();

    /*int threads = 10;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    try {
      final CountDownLatch latch = new CountDownLatch(threads);
      for (int n = 0; n < threads; n++) {
        pool.execute(new Runnable() {
          @Override
          public void run() {
            for (int i = 0; i < 1000; i++) {
              helloClient.send("hello world");
            }
            latch.countDown();
          }
        });
      }
      latch.await();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pool.shutdown();
    }*/

    int threads = 10;
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    try {
      final CountDownLatch begin = new CountDownLatch(1);
      final CountDownLatch end = new CountDownLatch(threads);
      for (int n = 0; n < threads; n++) {
        pool.execute(new Runnable() {
          @Override
          public void run() {
            try {
              begin.await();
              for (int i = 0; i < 1000; i++) {
                helloClient.send("hello world");
              }
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              end.countDown();
            }
          }
        });
      }
      begin.countDown();
      end.await();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pool.shutdown();
    }

    watch.stop();
    System.out.println("time: " + watch.getTotalTimeMillis() + "ms"); // time: 356ms
  }

  public static void main(String[] args) {
    SpringApplication.run(HelloClientApplication.class, args).close();
  }
}

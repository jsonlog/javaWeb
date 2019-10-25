package demo.msa.product;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DropwizardHelloApplication {

  @Bean
  public ServletRegistrationBean registerAdminServlet() {
    return new ServletRegistrationBean(new AdminServlet(), "/metrics/*");
  }

  @Bean
  public ServletListenerRegistrationBean<MetricsServlet.ContextListener> registerMetricRegistry() {
    return new ServletListenerRegistrationBean<MetricsServlet.ContextListener>(new MetricsServlet.ContextListener() {
      @Override
      protected MetricRegistry getMetricRegistry() {
        return new MetricRegistry();
      }
    });
  }

  @Bean
  public ServletListenerRegistrationBean<HealthCheckServlet.ContextListener> registerHealthCheckRegistry() {
    return new ServletListenerRegistrationBean<HealthCheckServlet.ContextListener>(new HealthCheckServlet.ContextListener() {
      @Override
      protected HealthCheckRegistry getHealthCheckRegistry() {
        return new HealthCheckRegistry();
      }
    });
  }

  public static void main(String[] args) {
    SpringApplication.run(DropwizardHelloApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello() {
    return "Hello";
  }
}

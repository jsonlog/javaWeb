package demo.msa.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GrpcServer implements ApplicationContextAware, InitializingBean {

  private List<BindableService> serviceBeanList = new ArrayList<>();

  @Value("${grpc.port}")
  private int port;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(GrpcService.class);
    if (serviceBeanMap != null && serviceBeanMap.size() != 0) {
      for (Object serviceBean : serviceBeanMap.values()) {
        if (serviceBean instanceof BindableService) {
          serviceBeanList.add((BindableService) serviceBean);
        }
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    ServerBuilder builder = ServerBuilder.forPort(port);
    for (BindableService serviceBean : serviceBeanList) {
      builder.addService(serviceBean);
    }
    Server server = builder.build().start();
    System.out.println("server started, listening on " + port);
    server.awaitTermination();
  }
}

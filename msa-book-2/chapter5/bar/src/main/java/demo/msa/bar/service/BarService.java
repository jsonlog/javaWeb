package demo.msa.bar.service;

import demo.msa.event.Event;
import demo.msa.event.EventManager;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BarService {

  private JdbcTemplate jdbcTemplate;
  private EventManager eventManager;

  @Autowired
  public BarService(JdbcTemplate jdbcTemplate, EventManager eventManager) {
    this.jdbcTemplate = jdbcTemplate;
    this.eventManager = eventManager;
  }

  @Transactional
  @RabbitListener(queues = "foo-success-queue")
  public void handleFooSuccess(Event event) {
    try {
      String barId = UUID.randomUUID().toString();
      jdbcTemplate.update(
          "INSERT INTO bar (id, name) VALUES (?, ?)",
          barId, "bar"
      );
      throw new RuntimeException(); // 故意抛出异常
    } catch (Exception e) {
      eventManager.sendEventQueue("bar-failure-queue", event);
      throw new AmqpRejectAndDontRequeueException(e);
    }
  }
}

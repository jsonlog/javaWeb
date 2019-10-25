package demo.msa.event;

import java.util.UUID;

public class Event {

  private String id;
  private EventType eventType;
  private String modelName;
  private String modelId;
  private long createdTime;

  public Event() {
  }

  public Event(EventType eventType, String modelName, String modelId) {
    this.id = UUID.randomUUID().toString();
    this.eventType = eventType;
    this.modelName = modelName;
    this.modelId = modelId;
    this.createdTime = System.currentTimeMillis();
  }

  public String getId() {
    return id;
  }

  public EventType getEventType() {
    return eventType;
  }

  public String getModelName() {
    return modelName;
  }

  public String getModelId() {
    return modelId;
  }

  public long getCreatedTime() {
    return createdTime;
  }
}

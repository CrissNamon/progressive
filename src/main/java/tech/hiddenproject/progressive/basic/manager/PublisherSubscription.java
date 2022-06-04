package tech.hiddenproject.progressive.basic.manager;

import java.util.*;

public final class PublisherSubscription {

  private final String topic;
  private final Long id;

  public PublisherSubscription(String topic, Long id) {
    this.topic = topic;
    this.id = id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    PublisherSubscription publisherSubscription = (PublisherSubscription) obj;
    return getTopic().equals(publisherSubscription.getTopic())
        && getId().equals(publisherSubscription.getId());
  }

  public String getTopic() {
    return topic;
  }

  public Long getId() {
    return id;
  }
}

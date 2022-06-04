package tech.hiddenproject.progressive.basic.manager;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import tech.hiddenproject.progressive.*;
import tech.hiddenproject.progressive.lambda.*;
import tech.hiddenproject.progressive.manager.*;

/**
 * Basic implementation of {@link GamePublisher}.
 *
 * <p>Allow you to subscribe on and make global events
 */
public class BasicGamePublisher implements GamePublisher<PublisherSubscription, String> {

  private static BasicGamePublisher INSTANCE;

  private final AtomicLong generator;

  private final Map<String, Map<Long, GameActionObject>> feed;
  private PublisherType publisherType;

  protected BasicGamePublisher() {
    feed = new ConcurrentHashMap<>();
    publisherType = PublisherType.PARALLEL;
    generator = new AtomicLong(0);
  }

  public static BasicGamePublisher getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BasicGamePublisher();
    }
    return INSTANCE;
  }

  @Override
  public void sendTo(String topic, Object message) {
    Map<Long, GameActionObject> subscribers = feed.getOrDefault(topic, new HashMap<>());
    switch (publisherType) {
      case SEQUENCE:
        sendSequence(message, subscribers);
        break;
      case PARALLEL:
        sendParallel(message, subscribers);
        break;
    }
  }

  @Override
  public <V> PublisherSubscription subscribeOn(String topic, GameActionObject<V> action) {
    feed.putIfAbsent(topic, new ConcurrentHashMap<>());
    long id = generator.incrementAndGet();
    feed.get(topic).put(id, action);
    return new PublisherSubscription(topic, id);
  }

  @Override
  public void unsubscribe(PublisherSubscription subscription) {
    feed.getOrDefault(subscription.getTopic(), new HashMap<>()).remove(subscription.getId());
  }

  @Override
  public PublisherType getPublisherType() {
    return publisherType;
  }

  @Override
  public void setPublisherType(PublisherType publisherType) {
    this.publisherType = publisherType;
  }

  @SuppressWarnings("unchecked")
  private void sendSequence(Object message, Map<Long, GameActionObject> subscribers) {
    Iterator<GameActionObject> iterator = subscribers.values().iterator();
    while (iterator.hasNext()) {
      iterator.next().make(message);
    }
  }

  @SuppressWarnings("unchecked")
  private void sendParallel(Object message, Map<Long, GameActionObject> subscribers) {
    subscribers.values().parallelStream().unordered().forEach(l -> l.make(message));
  }
}

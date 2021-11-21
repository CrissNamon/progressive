package ru.hiddenproject.progressive.manager;

import ru.hiddenproject.progressive.PublisherType;
import ru.hiddenproject.progressive.lambda.GameActionObject;

/**
 * Represents some publisher components can use to `talk` with each other
 * <p>Components can subscribe to some topics or send messages to some topics</p>
 */
public interface GamePublisher<I> {

  /**
   * Sends {@code message} to {@code topic}.
   *
   * @param topic Topic to send message to.
   * @param message Message to send
   */
  void sendTo(String topic, Object message);

  /**
   * Subscribe to {@code topic} with {@code action}.
   *
   * @param topic Topic to subscribe on.
   * @param action Action to make on new message
   * @param <V> Type to receive in action
   */
  <V> I subscribeOn(String topic, GameActionObject<V> action);

  /**
   * Removes listener from topic.
   *
   * @param subscription Subscription data to unsubscribe
   */
  void unsubscribe(I subscription);

  /**
   * Sets {@link ru.hiddenproject.progressive.PublisherType}.
   * See {@link ru.hiddenproject.progressive.PublisherType} for more information.
   *
   * @param publisherType Type to set
   */
  void setPublisherType(PublisherType publisherType);

  /**
   * Returns current {@link ru.hiddenproject.progressive.PublisherType}.
   *
   * @return Current type
   */
  PublisherType getPublisherType();

}
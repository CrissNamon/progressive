package tech.hiddenproject.progressive.manager;

import tech.hiddenproject.progressive.PublisherType;
import tech.hiddenproject.progressive.basic.lambda.GameActionObject;

/**
 * Represents some publisher components can use to `talk` with each other.
 *
 * <p>Components can subscribe to some topics or send messages to some topics
 */
public interface GamePublisher<I, T> {

  /**
   * Sends {@code message} to {@code topic}.
   *
   * @param topic   Topic to send message to.
   * @param message Message to send
   */
  void sendTo(T topic, Object message);

  /**
   * Subscribe to {@code topic} with {@code action}.
   *
   * @param topic  Topic to subscribe on.
   * @param action Action to make on new message
   * @param <V>    Type to receive in action
   */
  <V> I subscribeOn(T topic, GameActionObject<V> action);

  /**
   * Removes listener from topic.
   *
   * @param subscription Subscription data to unsubscribe
   */
  void unsubscribe(I subscription);

  /**
   * Returns current {@link PublisherType}.
   *
   * @return Current type
   */
  PublisherType getPublisherType();

  /**
   * Sets {@link PublisherType}. See {@link PublisherType} for more information.
   *
   * @param publisherType Type to set
   */
  void setPublisherType(PublisherType publisherType);
}

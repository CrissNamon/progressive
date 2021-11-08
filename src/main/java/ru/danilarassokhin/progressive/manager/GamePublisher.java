package ru.danilarassokhin.progressive.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;

/**
 * Represents some publisher components can use to `talk` with each other
 * <p>Components can subscribe to some topics or send messages to some topics</p>
 */
public interface GamePublisher {

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
  <V> void subscribeOn(String topic, GameActionObject<V> action);

}

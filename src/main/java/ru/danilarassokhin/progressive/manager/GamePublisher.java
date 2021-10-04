package ru.danilarassokhin.progressive.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;

/**
 * Represents some publisher components can use to `talk` with each other
 * <p>Components can subscribe to some topics or send messages to some topics</p>
 */
public interface GamePublisher {

    void sendTo(String topic, Object message);

    <V> void subscribeOn(String topic, GameActionObject<V> action);

}

package ru.danilarassokhin.progressive.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;

public interface GamePublisher {

    void sendTo(String topic, Object message);

    <V> void subscribeOn(String topic, GameActionObject<V> action);

}

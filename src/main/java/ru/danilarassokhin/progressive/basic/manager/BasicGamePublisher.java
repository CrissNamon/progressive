package ru.danilarassokhin.progressive.basic.manager;

import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GamePublisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BasicGamePublisher implements GamePublisher {

    private static BasicGamePublisher INSTANCE;

    private final Map<String, List<GameActionObject>> feed;

    private BasicGamePublisher() {
        feed = new HashMap<>();
    }

    public static BasicGamePublisher getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGamePublisher();
        }
        return INSTANCE;
    }

    @Override
    public void sendTo(String topic, Object message) {
        List<GameActionObject> subscribers = feed.getOrDefault(topic, new ArrayList<>());
        subscribers.parallelStream().forEach(s -> s.make(message));
    }

    @Override
    public <V> void subscribeOn(String topic, GameActionObject<V> action) {
        feed.putIfAbsent(topic, new ArrayList<>());
        feed.get(topic).add(action);
    }
}

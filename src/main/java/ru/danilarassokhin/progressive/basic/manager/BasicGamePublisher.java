package ru.danilarassokhin.progressive.basic.manager;

import ru.danilarassokhin.progressive.basic.BasicGame;
import ru.danilarassokhin.progressive.lambda.GameActionObject;
import ru.danilarassokhin.progressive.manager.GamePublisher;

import java.util.*;

public final class BasicGamePublisher implements GamePublisher {

    private static BasicGamePublisher INSTANCE;

    private final Map<String, List<GameActionObject<Object>>> feed;

    private BasicGamePublisher() {
        feed = new LinkedHashMap<>();
    }

    public static BasicGamePublisher getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BasicGamePublisher();
        }
        return INSTANCE;
    }

    @Override
    public void sendTo(String topic, Object message) {
        List<GameActionObject<Object>> subscribers = feed.getOrDefault(topic, new ArrayList<>());
        send(message, subscribers);
    }

    @Override
    public <V> void subscribeOn(String topic, GameActionObject<V> action) {
        feed.putIfAbsent(topic, new ArrayList<>());
        feed.get(topic).add((GameActionObject) action);
    }

    private void send(Object message, Collection<GameActionObject<Object>> subscribers) {
        switch (BasicGame.getInstance().getFrameTimeType()) {
            case SEQUENCE:
                Iterator<GameActionObject<Object>> iterator = subscribers.iterator();
                while (iterator.hasNext()) {
                    iterator.next().make(message);
                }
                break;
            case PARALLEL:
                subscribers.parallelStream().forEach(s -> s.make(message));
                break;
        }
    }
}

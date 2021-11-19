package ru.danilarassokhin.example.publisher;

import ru.danilarassokhin.example.game.component.GameItem;
import ru.danilarassokhin.progressive.PublisherType;
import ru.danilarassokhin.progressive.basic.BasicComponentManager;
import ru.danilarassokhin.progressive.basic.manager.BasicGamePublisher;

public class PublisherExample {

  String message;

  public PublisherExample() {
    //Get publisher instance
    BasicGamePublisher basicGamePublisher = BasicGamePublisher.getInstance();

    //Set publisher type
    //PARALLEL - use parallel streams to call listeners
    //SEQUENCE - iterate through listeners in order they subscribed
    basicGamePublisher.setPublisherType(PublisherType.PARALLEL);

    //Subscribe to topic "print" and use print method while listener call
    basicGamePublisher.subscribeOn("method", this::print);
    //Subscribe to topic "print" and print received object to console
    basicGamePublisher.subscribeOn("print", m -> System.out.println(m));
    //Subscribe to topic "print" and set current message to received message
    basicGamePublisher.subscribeOn("set", m -> message = m.toString());

    //Send message "hello" to "method" topic, e.g. call all listeners on "method" topic and pass "hello" to them
    basicGamePublisher.sendTo("method", "hello");
    //Send message "message" to "print" topic, e.g. call all listeners on "print" and pass "message" to them
    basicGamePublisher.sendTo("print", "message");
    //Send GameItem object to "set" topic, e.g. call all listeners on "set" and pass GameItem to them
    basicGamePublisher.sendTo("set", new GameItem());
  }

  public void print(String message) {
    BasicComponentManager
        .getGameLogger().info(message);
  }

}

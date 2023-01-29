package tech.hiddenproject.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.basic.manager.BasicGamePublisher;
import tech.hiddenproject.progressive.basic.manager.PublisherSubscription;

/**
 * @author Danila Rassokhin
 */
public class PublisherTest {

  private static final String TOPIC = "TEST_TOPIC";

  private static int counter;

  @Test
  public void subscriptionTest() {
    BasicGamePublisher gamePublisher = BasicGamePublisher.getInstance();
    PublisherSubscription subscription = gamePublisher.subscribeOn(TOPIC, this::consumer);

    gamePublisher.sendTo(TOPIC, new Object());
    gamePublisher.unsubscribe(subscription);
    gamePublisher.sendTo(TOPIC, new Object());

    Assertions.assertEquals(1, counter);
  }

  private void consumer(Object object) {
    counter++;
  }

}

package tech.hiddenproject.progressive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.hiddenproject.progressive.basic.lambda.StateMachineGuard;
import tech.hiddenproject.progressive.basic.manager.BasicStateMachine;
import tech.hiddenproject.progressive.data.StateMachineTransition;
import tech.hiddenproject.progressive.data.TestAction;
import tech.hiddenproject.progressive.data.TestEvent;
import tech.hiddenproject.progressive.data.TestState;
import tech.hiddenproject.progressive.exception.StateMachineException;
import tech.hiddenproject.progressive.manager.StateMachine;

/**
 * @author Danila Rassokhin
 */
public class StateMachineTest {

  @Test
  public void validStateMachineTest() {
    StateMachine<TestState, TestEvent, Object> stateMachine = BasicStateMachine.<TestState, TestEvent, Object> create()
        .transition(TestState.STATE1, TestState.STATE2)
        .event(TestEvent.EVENT1)
        .and()
        .transition(TestState.STATE2, TestState.STATE3)
        .event(TestEvent.EVENT2)
        .forPayload(new Object())
        .build(TestState.STATE1);

    Assertions.assertDoesNotThrow(() -> stateMachine.post(TestEvent.EVENT1));
    Assertions.assertDoesNotThrow(() -> stateMachine.fire(TestEvent.EVENT2));
  }

  @Test
  public void validStateMachineFireWrongEventTest() {
    StateMachine<TestState, TestEvent, Object> stateMachine = BasicStateMachine.<TestState, TestEvent, Object> create()
        .transition(TestState.STATE1, TestState.STATE2)
        .event(TestEvent.EVENT1)
        .and()
        .transition(TestState.STATE2, TestState.STATE3)
        .event(TestEvent.EVENT2)
        .forPayload(new Object())
        .build(TestState.STATE1);

    Assertions.assertDoesNotThrow(() -> stateMachine.fire(TestEvent.EVENT1));
    Assertions.assertThrows(StateMachineException.class, () -> stateMachine.fire(TestEvent.EVENT3));
  }

  @Test
  public void validStateMachineGuardTest() {
    StateMachine<TestState, TestEvent, Object> stateMachine = BasicStateMachine.<TestState, TestEvent, Object> create()
        .transition(TestState.STATE1, TestState.STATE2)
        .event(TestEvent.EVENT1)
        .guard(createFalseGuard())
        .and()
        .transition(TestState.STATE2, TestState.STATE3)
        .event(TestEvent.EVENT2)
        .guard(createTrueGuard())
        .forPayload(new Object())
        .build(TestState.STATE1);

    Assertions.assertDoesNotThrow(() -> stateMachine.fire(TestEvent.EVENT1));
    Assertions.assertThrows(StateMachineException.class, () -> stateMachine.fire(TestEvent.EVENT2));
  }

  @Test
  public void validStateMachineActionTest() {
    TestAction action = new TestAction();
    StateMachine<TestState, TestEvent, Object> stateMachine = BasicStateMachine.<TestState, TestEvent, Object> create()
        .transition(TestState.STATE1, TestState.STATE2)
        .event(TestEvent.EVENT1)
        .action(action)
        .and()
        .transition(TestState.STATE2, TestState.STATE3)
        .event(TestEvent.EVENT2)
        .action(action)
        .forPayload(new Object())
        .build(TestState.STATE1);
    stateMachine.post(TestEvent.EVENT1);
    stateMachine.post(TestEvent.EVENT2);
    Assertions.assertEquals(2, action.getCounter());
  }

  private StateMachineGuard<TestState, TestEvent> createTrueGuard() {
    return new StateMachineGuard<TestState, TestEvent>() {
      @Override
      public boolean test(StateMachineTransition<TestState, TestEvent> transition) {
        return true;
      }

      @Override
      public String getName() {
        return "true guard";
      }
    };
  }

  private StateMachineGuard<TestState, TestEvent> createFalseGuard() {
    return new StateMachineGuard<TestState, TestEvent>() {
      @Override
      public boolean test(StateMachineTransition<TestState, TestEvent> transition) {
        return false;
      }

      @Override
      public String getName() {
        return "false guard";
      }
    };
  }
}

package tech.hiddenproject.example.statemachine;

import java.util.UUID;
import tech.hiddenproject.progressive.BasicComponentManager;
import tech.hiddenproject.progressive.basic.lambda.StateMachineGuard;
import tech.hiddenproject.progressive.basic.manager.BasicStateMachine;
import tech.hiddenproject.progressive.data.StateMachineTransition;
import tech.hiddenproject.progressive.exception.StateMachineException;
import tech.hiddenproject.progressive.manager.StateMachine;

/**
 * @author Danila Rassokhin
 */
public class StateMachineExample {

  private final UUID id = UUID.randomUUID();
  private boolean isGuardShouldBeTriggered = false;

  public StateMachineExample() {
    StateMachine<ExampleState, ExampleEvent, UUID> stateMachine = buildMachine();

    BasicComponentManager.getGameLogger().info("Initial state: " + stateMachine.getState());
    stateMachine.post(ExampleEvent.EVENT1);

    BasicComponentManager.getGameLogger().info(stateMachine.getState());
    stateMachine.post(ExampleEvent.EVENT2);

    BasicComponentManager.getGameLogger().info(stateMachine.getState());
    stateMachine.post(ExampleEvent.EVENT3);

    BasicComponentManager.getGameLogger().info(stateMachine.getState());

    try {
      isGuardShouldBeTriggered = true;
      stateMachine.post(ExampleEvent.EVENT1);
      stateMachine.post(ExampleEvent.EVENT2);
    } catch (StateMachineException e) {
      BasicComponentManager.getGameLogger().info("Guard will be triggered here!");
      BasicComponentManager.getGameLogger().info(e.getMessage());
    }

    try {
      stateMachine.fire(ExampleEvent.EVENT1);
    } catch (StateMachineException e) {
      BasicComponentManager.getGameLogger()
          .info("Transition for fired event not found, so got exception");
      BasicComponentManager.getGameLogger().info(e.getMessage());
    }

  }

  private StateMachine<ExampleState, ExampleEvent, UUID> buildMachine() {
    return BasicStateMachine.<ExampleState, ExampleEvent, UUID> create()
        .transition(ExampleState.STATE1, ExampleState.STATE2)
        .event(ExampleEvent.EVENT1)
        .action(this::printTransition)
        .and()
        .transition(ExampleState.STATE2, ExampleState.STATE3)
        .event(ExampleEvent.EVENT2)
        .guard(createGuard())
        .action(this::printTransition)
        .and()
        .transition(ExampleState.STATE3, ExampleState.STATE1)
        .event(ExampleEvent.EVENT3)
        .action(this::printTransition)
        .forPayload(id)
        .build(ExampleState.STATE1);
  }

  private StateMachineGuard<ExampleState, ExampleEvent> createGuard() {
    return new StateMachineGuard<ExampleState, ExampleEvent>() {
      @Override
      public boolean test(StateMachineTransition<ExampleState, ExampleEvent> transition) {
        return isGuardShouldBeTriggered;
      }

      @Override
      public String getName() {
        return "Example guard";
      }
    };
  }

  private void printTransition(StateMachineTransition<ExampleState, ExampleEvent> transition) {
    BasicComponentManager.getGameLogger()
        .info("Moved from " + transition.getFrom() + " on " + transition.getTo());
  }
}

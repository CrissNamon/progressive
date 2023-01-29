package tech.hiddenproject.progressive.util;

import tech.hiddenproject.progressive.basic.lambda.GameActionObject;

/**
 * Basic implementation of object caster.
 */
public final class BasicObjectCaster implements GameObjectCaster {

  public <T, O> T cast(O from, Class<T> to) {
    return cast(from, to, null);
  }

  @Override
  public <T, O> T cast(O from, Class<T> to, GameActionObject<T> onSuccessCast) {
    try {
      T casted = to.cast(from);
      if (onSuccessCast != null) {
        onSuccessCast.make(casted);
      }
      return casted;
    } catch (ClassCastException e) {
      return null;
    }
  }
}

package ru.hiddenproject.progressive.basic.util;

import ru.hiddenproject.progressive.lambda.GameActionObject;
import ru.hiddenproject.progressive.util.GameObjectCaster;

/**
 * Basic implementation of object caster
 */
public final class BasicObjectCaster implements GameObjectCaster {

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

  public <T, O> T cast(O from, Class<T> to) {
    return cast(from, to, null);
  }
}
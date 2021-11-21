package ru.hiddenproject.progressive.util;

import ru.hiddenproject.progressive.lambda.GameActionObject;

/**
 * Casts one object type to another
 */
public interface GameObjectCaster {

  /**
   * @param from          Original object
   * @param to            Object you want to cast to
   * @param onSuccessCast Will be called on successful cast
   * @param <T>           Object's class you want to cast to
   * @param <O>           Object you want to cast from
   * @return Casted object if casted successfully, null otherwise
   */
  <T, O> T cast(O from, Class<T> to, GameActionObject<T> onSuccessCast);

}
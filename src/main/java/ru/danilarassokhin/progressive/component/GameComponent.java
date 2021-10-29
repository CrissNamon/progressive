package ru.danilarassokhin.progressive.component;

import java.io.Serializable;

/**
 * Abstract game component
 */
public interface GameComponent extends Serializable {

  /**
   * @return Component id
   */
  Long getId();

}

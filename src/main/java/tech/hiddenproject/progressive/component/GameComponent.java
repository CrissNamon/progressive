package tech.hiddenproject.progressive.component;

import java.io.*;

/** Abstract game component. */
public interface GameComponent extends Serializable {

  /** @return Component id */
  Long getId();
}

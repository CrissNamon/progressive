package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.*;
import tech.hiddenproject.progressive.injection.*;

/** Used to mark class as bean provider for {@link DIContainer}. */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {}

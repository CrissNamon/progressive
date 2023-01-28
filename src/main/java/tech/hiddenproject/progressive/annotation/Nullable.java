package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation on {@link tech.hiddenproject.progressive.storage.StorageEntity}'s field indicates
 * if fields with null values will be saved as
 * {@link tech.hiddenproject.progressive.storage.EntityMetadata}.
 *
 * @author Danila Rassokhin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Nullable {

}

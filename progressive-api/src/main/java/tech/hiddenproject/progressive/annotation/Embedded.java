package tech.hiddenproject.progressive.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation on {@link tech.hiddenproject.progressive.storage.StorageEntity}'s field indicates
 * that this field is a complex type and fields of that type must be processed as entity fields.
 * E.g. entity MyEntity has a field named myClass with type MyClass and MyClass has field myField.
 * If myClass is annotated with @Embedded then
 * {@link tech.hiddenproject.progressive.storage.EntityMetadata} of MyEntity will contain field with
 * name myEntity.myField and value of myField from myClass. Otherwise, myClass field won't be added
 * to {@link tech.hiddenproject.progressive.storage.EntityMetadata}
 *
 * @author Danila Rassokhin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Embedded {

}

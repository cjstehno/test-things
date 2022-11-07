package io.github.cjstehno.testthings.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used by the <code>ResourcesExtension</code> to annotate fields and parameters that should be populated
 * with resource paths or data.
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Resource {

    /**
     * The resource classpath path.
     *
     * @return the path to the resource relative to the classpath
     */
    String value();
}

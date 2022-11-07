package io.github.cjstehno.testthings.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Resource {

    // FIXME: the path
    String value();

    /*
        FIXME: document
        - when annotating a File or Path -> injects the resolved file or path value
        - when annotating a String, byte[] or InputStream -> injects the resolved resource
        - when annotating an object (other than above) -> deserialize (?) how to determine what used and type?
     */
}

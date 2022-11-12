/**
 * Copyright (C) 2022 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.cjstehno.testthings.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used by the {@link DatabaseExtension} to define the setup and teardown SQL scripts.
 *
 * This annotation may be applied at the class level, providing default behavior for all tests, and/or it may be provided
 * at the test method level to add or override configuration data.
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface PrepareDatabase {

    /**
     * Defines the method (by name) on the test instance (static or non-static) which will be executed to create
     * the {@link javax.sql.DataSource} instance used by the extension. The method must return a {@link javax.sql.DataSource}
     * instance.
     *
     * @return the DataSource create method.
     */
    String creator() default "";

    /**
     * A list of setup SQL scripts which will be run in order to setup the database.
     *
     * @return the list of scripts
     */
    String[] setup() default "";

    /**
     * A list of tear-down SQL scripts which will be run in order to tear down the database.
     *
     * @return the list of scripts
     */
    String[] teardown() default "";

    /**
     * Defines whether the setup and teardown scripts will be added to or overrides of any defined at the class level.
     *
     * @return whether the scripts are additive or overriding
     */
    boolean additive() default true;

    /**
     * Defines the method (by name) on the test instance (static or non-static) which will be executed to destroy the
     * {@link javax.sql.DataSource} instance used by the extension. The method must accept a {@link javax.sql.DataSource}
     * parameter.
     *
     * @return the name of the method used to destroy the DataSource
     */
    String destroyer() default "";
}

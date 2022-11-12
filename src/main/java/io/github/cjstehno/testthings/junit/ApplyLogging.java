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

import java.lang.annotation.*;

/**
 * When used with the <code>LogAppenderExtension</code> on a test method, it will use the <code>AppenderConfig</code>
 * or <code>Consumer&lt;AppenderConfig&gt;</code> field named in the value property as the configuration for the log
 * appender.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApplyLogging {

    /**
     * The name of the static field containing an <code>AppenderConfig</code> or <code>Consumer&lt;AppenderConfig&gt;</code> object.
     *
     * @return the field name
     */
    String value();
}

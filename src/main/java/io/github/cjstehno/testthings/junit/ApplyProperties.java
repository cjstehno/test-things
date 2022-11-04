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
 * When used with the <code>SystemPropertiesExtension</code> on a test method, it will use the <code>Properties</code>
 * or <code>Map</code> object data as the properties to be injected.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApplyProperties {

    /**
     * The name of the static field containing a <code>Properties</code> or <code>Map&lt;String,String&gt;</code> object.
     *
     * @return the field name
     */
    String value();
}

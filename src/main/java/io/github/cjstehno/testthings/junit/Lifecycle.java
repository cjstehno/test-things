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
 * Annotation used by the {@link LifecycleExtension} to annotate methods that should be run by the extension.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lifecycle {

    /**
     * The test lifecycle extension point identifier.
     */
    enum LifecyclePoint {
        BEFORE_ALL, BEFORE_EACH, AFTER_EACH, AFTER_ALL;
    }

    /**
     * The lifecycle extension point represented by the annotation.
     *
     * @return the extension point
     */
    LifecyclePoint value();
}

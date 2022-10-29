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
package io.github.cjstehno.testthings.fixtures;

import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.StringUtils.capitalize;

public enum FemaleName {
    OLIVIA,
    EMMA,
    CHARLOTTE,
    AMELIA,
    AVA,
    SOPHIA,
    ISABELLA,
    MIA,
    EVELYN,
    HARPER,
    LUNA,
    CAMILA,
    GIANNA,
    ELIZABETH,
    ELEANOR,
    ELLA,
    ABIGAIL,
    SOFIA,
    AVERY,
    SCARLETT,
    EMILY,
    ARIA,
    PENELOPE,
    CHLOE,
    LAYLA,
    MILA,
    NORA,
    HAZEL,
    MADISON,
    ELLIE,
    LILY,
    NOVA,
    ISLA,
    GRACE,
    VIOLET,
    AURORA,
    RILEY,
    ZOEY,
    WILLOW,
    EMILIA,
    STELLA,
    ZOE,
    VICTORIA,
    HANNAH,
    ADDISON,
    LEAH,
    LUCY,
    ELIANA,
    IVY,
    EVERLY;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

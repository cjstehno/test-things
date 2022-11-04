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

/**
 * An enumeration of some female names.
 */
public enum FemaleName {
    /**
     * Olivia.
     */
    OLIVIA,

    /**
     * Emma.
     */
    EMMA,

    /**
     * Charlotte.
     */
    CHARLOTTE,

    /**
     * Amelia.
     */
    AMELIA,

    /**
     * Ava.
     */
    AVA,

    /**
     * Sophia.
     */
    SOPHIA,

    /**
     * Isabella.
     */
    ISABELLA,

    /**
     * Mia.
     */
    MIA,

    /**
     * Evelyn.
     */
    EVELYN,

    /**
     * Harper.
     */
    HARPER,

    /**
     * Luna.
     */
    LUNA,

    /**
     * Camila.
     */
    CAMILA,

    /**
     * Gianna.
     */
    GIANNA,

    /**
     * Elizabeth.
     */
    ELIZABETH,

    /**
     * Elenor.
     */
    ELEANOR,

    /**
     * Ella.
     */
    ELLA,

    /**
     * Abigail.
     */
    ABIGAIL,

    /**
     * Sofia.
     */
    SOFIA,

    /**
     * Avery.
     */
    AVERY,

    /**
     * Scarlett.
     */
    SCARLETT,

    /**
     * Emily.
     */
    EMILY,

    /**
     * Aria.
     */
    ARIA,

    /**
     * Penelope.
     */
    PENELOPE,

    /**
     * Chloe.
     */
    CHLOE,

    /**
     * Layla.
     */
    LAYLA,

    /**
     * Mila.
     */
    MILA,

    /**
     * Nora.
     */
    NORA,

    /**
     * Hazel.
     */
    HAZEL,

    /**
     * Madison.
     */
    MADISON,

    /**
     * Ellie.
     */
    ELLIE,

    /**
     * Lily.
     */
    LILY,

    /**
     * Nova.
     */
    NOVA,

    /**
     * Isla.
     */
    ISLA,

    /**
     * Grace.
     */
    GRACE,

    /**
     * Violet.
     */
    VIOLET,

    /**
     * Aurora.
     */
    AURORA,

    /**
     * Riley.
     */
    RILEY,

    /**
     * Zoey.
     */
    ZOEY,

    /**
     * Willow.
     */
    WILLOW,

    /**
     * Emilia.
     */
    EMILIA,

    /**
     * Stella!
     */
    STELLA,

    /**
     * Zoe.
     */
    ZOE,

    /**
     * Victoria.
     */
    VICTORIA,

    /**
     * Hannah, without her sisters.
     */
    HANNAH,

    /**
     * Addison.
     */
    ADDISON,

    /**
     * Leah.
     */
    LEAH,

    /**
     * Lucy.
     */
    LUCY,

    /**
     * Eliana.
     */
    ELIANA,

    /**
     * Ivy.
     */
    IVY,

    /**
     * Everly.
     */
    EVERLY;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

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
 * An enumeration of some color names.
 */
public enum ColorName {

    /**
     * White.
     */
    WHITE,

    /**
     * Yellow.
     */
    YELLOW,

    /**
     * Blue.
     */
    BLUE,

    /**
     * Red.
     */
    RED,

    /**
     * Green.
     */
    GREEN,

    /**
     * Black.
     */
    BLACK,

    /**
     * Brown.
     */
    BROWN,

    /**
     * Azure.
     */
    AZURE,

    /**
     * Ivory.
     */
    IVORY,

    /**
     * Teal.
     */
    TEAL,

    /**
     * Silver.
     */
    SILVER,

    /**
     * Purple.
     */
    PURPLE,

    /**
     * Navy blue.
     */
    NAVY_BLUE,

    /**
     * Pea Green.
     */
    PEA_GREEN,

    /**
     * Gray.
     */
    GRAY,

    /**
     * Orange.
     */
    ORANGE,

    /**
     * Maroon.
     */
    MAROON,

    /**
     * Charcoal.
     */
    CHARCOAL,

    /**
     * Aquamarine.
     */
    AQUAMARINE,

    /**
     * Coral (no, not Rick's son)
     */
    CORAL,

    /**
     * Fuchsia.
     */
    FUCHSIA,

    /**
     * Wheat.
     */
    WHEAT,

    /**
     * Lime.
     */
    LIME,

    /**
     * Crimson.
     */
    CRIMSON,

    /**
     * Khaki.
     */
    KHAKI,

    /**
     * Hot Pink.
     */
    HOT_PINK,

    /**
     * Magenta.
     */
    MAGENTA,

    /**
     * Olden.
     */
    OLDEN,

    /**
     * Plum.
     */
    PLUM,

    /**
     * Olive.
     */
    OLIVE,

    /**
     * Cyan.
     */
    CYAN;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

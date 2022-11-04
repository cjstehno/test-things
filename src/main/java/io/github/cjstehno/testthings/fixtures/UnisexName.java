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
 * An enumeration of some unisex names.
 */
public enum UnisexName {

    /**
     * Logan.
     */
    LOGAN,

    /**
     * Avery.
     */
    AVERY,

    /**
     * Riley.
     */
    RILEY,

    /**
     * Parker.
     */
    PARKER,

    /**
     * Cameron.
     */
    CAMERON,

    /**
     * Ryan.
     */
    RYAN,

    /**
     * River.
     */
    RIVER,

    /**
     * Angel.
     */
    ANGEL,

    /**
     * Rowan.
     */
    ROWAN,

    /**
     * Jordan.
     */
    JORDAN,

    /**
     * Sawyer.
     */
    SAWYER,

    /**
     * Charlie.
     */
    CHARLIE,

    /**
     * Quinn.
     */
    QUINN,

    /**
     * Blake.
     */
    BLAKE,

    /**
     * Peyton.
     */
    PEYTON,

    /**
     * Hayden.
     */
    HAYDEN,

    /**
     * Emery.
     */
    EMERY,

    /**
     * Emerson.
     */
    EMERSON,

    /**
     * Amari.
     */
    AMARI,

    /**
     * Eden.
     */
    EDEN,

    /**
     * Elliott.
     */
    ELLIOTT,

    /**
     * Elliot.
     */
    ELLIOT,

    /**
     * Finley.
     */
    FINLEY,

    /**
     * Remi.
     */
    REMI,

    /**
     * Remington.
     */
    REMINGTON,

    /**
     * Phoenix.
     */
    PHOENIX,

    /**
     * Sage.
     */
    SAGE,

    /**
     * Oakley.
     */
    OAKLEY,

    /**
     * Reese.
     */
    REESE,

    /**
     * Karter.
     */
    KARTER,

    /**
     * Dakota.
     */
    DAKOTA,

    /**
     * Tatum.
     */
    TATUM,

    /**
     * Taylor.
     */
    TAYLOR,

    /**
     * Rory.
     */
    RORY,

    /**
     * Morgan.
     */
    MORGAN,

    /**
     * Lennox.
     */
    LENNOX,

    /**
     * Ariel.
     */
    ARIEL,

    /**
     * Sutton.
     */
    SUTTON,

    /**
     * Dallas.
     */
    DALLAS,

    /**
     * Aspen.
     */
    ASPEN,

    /**
     * Lennon.
     */
    LENNON,

    /**
     * Ari.
     */
    ARI,

    /**
     * Alexis.
     */
    ALEXIS,

    /**
     * Marley.
     */
    MARLEY,

    /**
     * London.
     */
    LONDON,

    /**
     * Armani.
     */
    ARMANI,

    /**
     * Ellis.
     */
    ELLIS,

    /**
     * Remy.
     */
    REMY,

    /**
     * Wren.
     */
    WREN,

    /**
     * Reign.
     */
    REIGN;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

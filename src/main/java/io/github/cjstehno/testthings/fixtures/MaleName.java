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
 * An enumeration of some male names.
 */
public enum MaleName {

    /**
     * Liam.
     */
    LIAM,

    /**
     * Noah.
     */
    NOAH,

    /**
     * Oliver.
     */
    OLIVER,

    /**
     * Elijah.
     */
    ELIJAH,

    /**
     * James.
     */
    JAMES,

    /**
     * William.
     */
    WILLIAM,

    /**
     * Benjamin.
     */
    BENJAMIN,

    /**
     * Lucas.
     */
    LUCAS,

    /**
     * Henry.
     */
    HENRY,

    /**
     * Theodore.
     */
    THEODORE,

    /**
     * Jack.
     */
    JACK,

    /**
     * Levi.
     */
    LEVI,

    /**
     * Alexander.
     */
    ALEXANDER,

    /**
     * Jackson.
     */
    JACKSON,

    /**
     * Mateo.
     */
    MATEO,

    /**
     * Daniel.
     */
    DANIEL,

    /**
     * Michael.
     */
    MICHAEL,

    /**
     * Mason.
     */
    MASON,

    /**
     * Sebastian.
     */
    SEBASTIAN,

    /**
     * Ethan.
     */
    ETHAN,

    /**
     * Logan.
     */
    LOGAN,

    /**
     * Owen.
     */
    OWEN,

    /**
     * Samuel.
     */
    SAMUEL,

    /**
     * Jacom.
     */
    JACOB,

    /**
     * Asher.
     */
    ASHER,

    /**
     * Aiden.
     */
    AIDEN,

    /**
     * John.
     */
    JOHN,

    /**
     * Joseph.
     */
    JOSEPH,

    /**
     * Wyatt.
     */
    WYATT,

    /**
     * David.
     */
    DAVID,

    /**
     * Leo.
     */
    LEO,

    /**
     * Luke, I am NOT your father.
     */
    LUKE,

    /**
     * Julian.
     */
    JULIAN,

    /**
     * Hudson.
     */
    HUDSON,

    /**
     * Grayson.
     */
    GRAYSON,

    /**
     * Matthew.
     */
    MATTHEW,

    /**
     * Ezra.
     */
    EZRA,

    /**
     * Gabriel.
     */
    GABRIEL,

    /**
     * Carter.
     */
    CARTER,

    /**
     * Isaac.
     */
    ISAAC,

    /**
     * Jayden.
     */
    JAYDEN,

    /**
     * Luca. Do you live on the second floor?
     */
    LUCA,

    /**
     * Anthony.
     */
    ANTHONY,

    /**
     * Dylan.
     */
    DYLAN,

    /**
     * Lincoln.
     */
    LINCOLN,

    /**
     * Thomas.
     */
    THOMAS,

    /**
     * Maverick.
     */
    MAVERICK,

    /**
     * Elias.
     */
    ELIAS,

    /**
     * Josiah.
     */
    JOSIAH,

    /**
     * CHARLES.
     */
    CHARLES;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

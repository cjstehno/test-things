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

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import static java.util.Locale.ROOT;
import static org.apache.commons.lang3.StringUtils.capitalize;

public enum MaleName {
    LIAM,
    NOAH,
    OLIVER,
    ELIJAH,
    JAMES,
    WILLIAM,
    BENJAMIN,
    LUCAS,
    HENRY,
    THEODORE,
    JACK,
    LEVI,
    ALEXANDER,
    JACKSON,
    MATEO,
    DANIEL,
    MICHAEL,
    MASON,
    SEBASTIAN,
    ETHAN,
    LOGAN,
    OWEN,
    SAMUEL,
    JACOB,
    ASHER,
    AIDEN,
    JOHN,
    JOSEPH,
    WYATT,
    DAVID,
    LEO,
    LUKE,
    JULIAN,
    HUDSON,
    GRAYSON,
    MATTHEW,
    EZRA,
    GABRIEL,
    CARTER,
    ISAAC,
    JAYDEN,
    LUCA,
    ANTHONY,
    DYLAN,
    LINCOLN,
    THOMAS,
    MAVERICK,
    ELIAS,
    JOSIAH,
    CHARLES;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

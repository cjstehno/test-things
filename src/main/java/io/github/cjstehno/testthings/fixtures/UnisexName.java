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

public enum UnisexName {
    LOGAN,
    AVERY,
    RILEY,
    PARKER,
    CAMERON,
    RYAN,
    RIVER,
    ANGEL,
    ROWAN,
    JORDAN,
    SAWYER,
    CHARLIE,
    QUINN,
    BLAKE,
    PEYTON,
    HAYDEN,
    EMERY,
    EMERSON,
    AMARI,
    EDEN,
    ELLIOTT,
    ELLIOT,
    FINLEY,
    REMI,
    REMINGTON,
    PHOENIX,
    SAGE,
    OAKLEY,
    REESE,
    KARTER,
    DAKOTA,
    TATUM,
    TAYLOR,
    RORY,
    MORGAN,
    LENNOX,
    ARIEL,
    SUTTON,
    DALLAS,
    ASPEN,
    LENNON,
    ARI,
    ALEXIS,
    MARLEY,
    LONDON,
    ARMANI,
    ELLIS,
    REMY,
    WREN,
    REIGN;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

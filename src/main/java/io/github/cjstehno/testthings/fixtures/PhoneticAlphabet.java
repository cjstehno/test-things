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
 * An enumeration of the military phonetic alphabet.
 */
public enum PhoneticAlphabet {

    /**
     * Alpha - A.
     */
    ALPHA,

    /**
     * Bravo - B.
     */
    BRAVO,

    /**
     * Charlie - C.
     */
    CHARLIE,

    /**
     * Delta - D.
     */
    DELTA,

    /**
     * Echo - E.
     */
    ECHO,

    /**
     * Foxtrot - F.
     */
    FOXTROT,

    /**
     * Golf - G.
     */
    GOLF,

    /**
     * Hotel - H.
     */
    HOTEL,

    /**
     * India - I.
     */
    INDIA,

    /**
     * Juliet - J.
     */
    JULIET,

    /**
     * Kilo - K.
     */
    KILO,

    /**
     * Lima - L.
     */
    LIMA,

    /**
     * Mike - M.
     */
    MIKE,

    /**
     * November - N.
     */
    NOVEMBER,

    /**
     * Oscar - O.
     */
    OSCAR,

    /**
     * Papa - P.
     */
    PAPA,

    /**
     * Quebec - Q.
     */
    QUEBEC,

    /**
     * Romeo - R.
     */
    ROMEO,

    /**
     * Sierra - S.
     */
    SIERRA,

    /**
     * Tango - T.
     */
    TANGO,

    /**
     * Uniform - U.
     */
    UNIFORM,

    /**
     * Victor - V.
     */
    VICTOR,

    /**
     * Whisky - W.
     */
    WHISKEY,

    /**
     * X-ray - X.
     */
    XRAY,

    /**
     * Yankee - Y.
     */
    YANKEE,

    /**
     * Zulu - Z.
     */
    ZULU;

    /**
     * Retrieves the letter associated with the phonetic item.
     *
     * @return the associated letter.
     */
    public String toLetter() {
        return String.valueOf(name().charAt(0));
    }

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

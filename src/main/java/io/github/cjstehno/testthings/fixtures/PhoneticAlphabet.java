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

public enum PhoneticAlphabet {

    ALPHA,
    BRAVO,
    CHARLIE,
    DELTA,
    ECHO,
    FOXTROT,
    GOLF,
    HOTEL,
    INDIA,
    JULIET,
    KILO,
    LIMA,
    MIKE,
    NOVEMBER,
    OSCAR,
    PAPA,
    QUEBEC,
    ROMEO,
    SIERRA,
    TANGO,
    UNIFORM,
    VICTOR,
    WHISKEY,
    XRAY,
    YANKEE,
    ZULU;

    public String toLetter() {
        return String.valueOf(name().charAt(0)).toLowerCase();
    }
}

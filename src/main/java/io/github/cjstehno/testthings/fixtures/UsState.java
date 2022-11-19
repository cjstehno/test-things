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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Locale.ROOT;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * An enumeration of US States.
 */
@RequiredArgsConstructor(access = PRIVATE)
public enum UsState {

    /**
     * Alabama.
     */
    ALABAMA("AL"),

    /**
     * Alaska.
     */
    ALASKA("AK"),

    /**
     * Arizona.
     */
    ARIZONA("AZ"),

    /**
     * Arkansas.
     */
    ARKANSAS("AR"),

    /**
     * California.
     */
    CALIFORNIA("CA"),

    /**
     * Colorado.
     */
    COLORADO("CO"),

    /**
     * Connecticut.
     */
    CONNECTICUT("CT"),

    /**
     * Delaware.
     */
    DELAWARE("DE"),

    /**
     * Florida.
     */
    FLORIDA("FL"),

    /**
     * Georgia.
     */
    GEORGIA("GA"),

    /**
     * Hawaii.
     */
    HAWAII("HI"),

    /**
     * Idaho.
     */
    IDAHO("ID"),

    /**
     * Illinois.
     */
    ILLINOIS("IL"),

    /**
     * Indiana.
     */
    INDIANA("IN"),

    /**
     * Iowa.
     */
    IOWA("IO"),

    /**
     * Kansas.
     */
    KANSAS("KA"),

    /**
     * Kentucky.
     */
    KENTUCKY("KT"),

    /**
     * Louisiana.
     */
    LOUISIANA("LA"),

    /**
     * Maine.
     */
    MAINE("ME"),

    /**
     * Maryland.
     */
    MARYLAND("MD"),

    /**
     * Massachusetts.
     */
    MASSACHUSETTS("MA"),

    /**
     * Michigan.
     */
    MICHIGAN("MI"),

    /**
     * Minnesota.
     */
    MINNESOTA("MN"),

    /**
     * Mississippi.
     */
    MISSISSIPPI("MS"),

    /**
     * Missouri.
     */
    MISSOURI("MO"),

    /**
     * Montana.
     */
    MONTANA("MT"),

    /**
     * Nebraska.
     */
    NEBRASKA("NE"),

    /**
     * Nevada.
     */
    NEVADA("NV"),

    /**
     * New Hampshire.
     */
    NEW_HAMPSHIRE("NH"),

    /**
     * New Jersey.
     */
    NEW_JERSEY("NJ"),

    /**
     * New Mexico.
     */
    NEW_MEXICO("NM"),

    /**
     * New York.
     */
    NEW_YORK("NY"),

    /**
     * North Carolina.
     */
    NORTH_CAROLINA("NC"),

    /**
     * North Dakota.
     */
    NORTH_DAKOTA("ND"),

    /**
     * Ohio.
     */
    OHIO("OH"),

    /**
     * Oklahoma.
     */
    OKLAHOMA("OK"),

    /**
     * Oregon.
     */
    OREGON("OR"),

    /**
     * Pennsylvania.
     */
    PENNSYLVANIA("PE"),

    /**
     * Rhode Island.
     */
    RHODE_ISLAND("RI"),

    /**
     * South Carolina.
     */
    SOUTH_CAROLINA("SC"),

    /**
     * South Dakota.
     */
    SOUTH_DAKOTA("SD"),

    /**
     * Tennessee.
     */
    TENNESSEE("TN"),

    /**
     * Texas.
     */
    TEXAS("TX"),

    /**
     * Utah.
     */
    UTAH("UT"),

    /**
     * Vermont.
     */
    VERMONT("VT"),

    /**
     * Virginia.
     */
    VIRGINIA("VA"),

    /**
     * Washington.
     */
    WASHINGTON("WA"),

    /**
     * West Virginia.
     */
    WEST_VIRGINIA("WV"),

    /**
     * Wisconsin.
     */
    WISCONSIN("WI"),

    /**
     * Wyoming.
     */
    WYOMING("WY");

    @Getter private final String abbreviation;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

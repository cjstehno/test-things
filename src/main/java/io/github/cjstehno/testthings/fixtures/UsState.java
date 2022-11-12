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
 * An enumeration of US States.
 */
public enum UsState {

    // TODO: add abbreviations

    /**
     * Alabama.
     */
    ALABAMA,

    /**
     * Alaska.
     */
    ALASKA,

    /**
     * Arizona.
     */
    ARIZONA,

    /**
     * Arkansas.
     */
    ARKANSAS,

    /**
     * California.
     */
    CALIFORNIA,

    /**
     * Colorado.
     */
    COLORADO,

    /**
     * Connecticut.
     */
    CONNECTICUT,

    /**
     * Delaware.
     */
    DELAWARE,

    /**
     * Florida.
     */
    FLORIDA,

    /**
     * Georgia.
     */
    GEORGIA,

    /**
     * Hawaii.
     */
    HAWAII,

    /**
     * Idaho.
     */
    IDAHO,

    /**
     * Illinois.
     */
    ILLINOIS,

    /**
     * Indiana.
     */
    INDIANA,

    /**
     * Iowa.
     */
    IOWA,

    /**
     * Kansas.
     */
    KANSAS,

    /**
     * Kentucky.
     */
    KENTUCKY,

    /**
     * Louisiana.
     */
    LOUISIANA,

    /**
     * Maine.
     */
    MAINE,

    /**
     * Maryland.
     */
    MARYLAND,

    /**
     * Massachusetts.
     */
    MASSACHUSETTS,

    /**
     * Michigan.
     */
    MICHIGAN,

    /**
     * Minnesota.
     */
    MINNESOTA,

    /**
     * Mississippi.
     */
    MISSISSIPPI,

    /**
     * Missouri.
     */
    MISSOURI,

    /**
     * Montana.
     */
    MONTANA,

    /**
     * Nebraska.
     */
    NEBRASKA,

    /**
     * Nevada.
     */
    NEVADA,

    /**
     * New Hampshire.
     */
    NEW_HAMPSHIRE,

    /**
     * New Jersey.
     */
    NEW_JERSEY,

    /**
     * New Mexico.
     */
    NEW_MEXICO,

    /**
     * New York.
     */
    NEW_YORK,

    /**
     * North Carolina.
     */
    NORTH_CAROLINA,

    /**
     * North Dakota.
     */
    NORTH_DAKOTA,

    /**
     * Ohio.
     */
    OHIO,

    /**
     * Oklahoma.
     */
    OKLAHOMA,

    /**
     * Oregon.
     */
    OREGON,

    /**
     * Pennsylvania.
     */
    PENNSYLVANIA,

    /**
     * Rhode Island.
     */
    RHODE_ISLAND,

    /**
     * South Carolina.
     */
    SOUTH_CAROLINA,

    /**
     * South Dakota.
     */
    SOUTH_DAKOTA,

    /**
     * Tennessee.
     */
    TENNESSEE,

    /**
     * Texas.
     */
    TEXAS,

    /**
     * Utah.
     */
    UTAH,

    /**
     * Vermont.
     */
    VERMONT,

    /**
     * Virginia.
     */
    VIRGINIA,

    /**
     * Washington.
     */
    WASHINGTON,

    /**
     * West Virginia.
     */
    WEST_VIRGINIA,

    /**
     * Wisconsin.
     */
    WISCONSIN,

    /**
     * Wyoming.
     */
    WYOMING;

    @Override public String toString() {
        return capitalize(name().toLowerCase(ROOT));
    }
}

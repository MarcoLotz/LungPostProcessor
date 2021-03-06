/*******************************************************************************
 * Copyright (c) 2002-2016 "Marco Aurelio Barbosa Fagnani Gomes Lotz"
 * [http://www.marcolotz.com]
 *
 * This file is part of Marco Lotz Hadoop Lung solution.
 *
 * Hadoop Lung is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.marcolotz.lung.posprocesser.data;

import org.apache.hadoop.conf.Configuration;

/**
 * Reference parameters used for false positives elimination. This parameters
 * are described in the paper: Zhao, B. ;Gamsu, G. ; Swartchz, M. Automatic
 * detection of small lung nodules on CT utilizing a local density maximum
 * algorithm
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ReferencesParameters {
	
	/***
	 * Has all user default configuration plus any command line configuration 
	 */
	public static Configuration conf;

	static double REFERENCE_R1_MAXIMUM = 0.3;
	static double REFERENCE_R2_ABOVE = 5.0;
	static double REFERENCE_R2_BELOW = 0.2;
	static double REFERENCE_R3_MINIMUM = 1.5;

	static double DXPSIZE_BELOW = 2.0;
	static double DYPSIZE_BELOW = 2.0;

	/***
	 * Original value was 15. Changed this parameter otherwise it would consider
	 * nodules with dimensions larger than 15 to be false positives.
	 */
	static double DXPSIZE_ABOVE = 40.0;
	static double DYPSIZE_ABOVE = 40.0;

	/***
	 * The distance of the centroid position of two nodule candidates in two
	 * adjacent slices. This value is used to consider is the two nodules
	 * candidates actually compose only one nodule.
	 */
	public static double CENTER_RESOLUTION = 2;
	
	
	/***
	 * Updates the fields of this method.
	 */
	public static void updateReferences()
	{
		REFERENCE_R1_MAXIMUM = conf.getDouble("com.marcolotz.nodule.r1Maximum", 0.3);

		REFERENCE_R2_ABOVE = conf.getDouble("com.marcolotz.nodule.r2Above", 5.0);
		REFERENCE_R2_BELOW = conf.getDouble("com.marcolotz.nodule.r2Below", 0.2);
		
		REFERENCE_R3_MINIMUM = conf.getDouble("com.marcolotz.nodule.r3Minimum", 1.5);
		
		DXPSIZE_BELOW = conf.getDouble("com.marcolotz.nodule.dxPsizeBelow", 2.0);
		DYPSIZE_BELOW = conf.getDouble("com.marcolotz.nodule.dyPsizeBelow", 2.0);
		
		DXPSIZE_ABOVE = conf.getDouble("com.marcolotz.nodule.dxPsizeAbove", 40);
		DYPSIZE_ABOVE = conf.getDouble("com.marcolotz.nodule.dyPsizeAbove", 40);
		
		CENTER_RESOLUTION = conf.getDouble("com.marcolotz.nodule.centroidDistance", 2);
	}

	/**
	 * @return the conf
	 */
	public static Configuration getConf() {
		return conf;
	}

	/**
	 * @return the rEFERENCE_R1_MAXIMUM
	 */
	public static double getREFERENCE_R1_MAXIMUM() {
		return REFERENCE_R1_MAXIMUM;
	}

	/**
	 * @return the rEFERENCE_R2_ABOVE
	 */
	public static double getREFERENCE_R2_ABOVE() {
		return REFERENCE_R2_ABOVE;
	}

	/**
	 * @return the rEFERENCE_R2_BELOW
	 */
	public static double getREFERENCE_R2_BELOW() {
		return REFERENCE_R2_BELOW;
	}

	/**
	 * @return the rEFERENCE_R3_MINIMUM
	 */
	public static double getREFERENCE_R3_MINIMUM() {
		return REFERENCE_R3_MINIMUM;
	}

	/**
	 * @return the dXPSIZE_BELOW
	 */
	public static double getDXPSIZE_BELOW() {
		return DXPSIZE_BELOW;
	}

	/**
	 * @return the dYPSIZE_BELOW
	 */
	public static double getDYPSIZE_BELOW() {
		return DYPSIZE_BELOW;
	}

	/**
	 * @return the dXPSIZE_ABOVE
	 */
	public static double getDXPSIZE_ABOVE() {
		return DXPSIZE_ABOVE;
	}

	/**
	 * @return the dYPSIZE_ABOVE
	 */
	public static double getDYPSIZE_ABOVE() {
		return DYPSIZE_ABOVE;
	}

	/**
	 * @return the cENTER_RESOLUTION
	 */
	public static double getCENTER_RESOLUTION() {
		return CENTER_RESOLUTION;
	}

	public static void printParameters() {
		System.out.println("Running application with the following false positive filtering parameters:");
		System.out.println("Any node that doesnt match those parameters will be removed.\n");

		System.out.println("R1 maximum: " + getREFERENCE_R1_MAXIMUM());
		System.out.println("R2 Above: " + getREFERENCE_R2_ABOVE());
		System.out.println("R2 Below: " + getREFERENCE_R2_BELOW());
		
		System.out.println("R3 minimum: " + getREFERENCE_R3_MINIMUM());
		
		System.out.println("Dx*Psize Below: " + getDXPSIZE_BELOW());
		System.out.println("Dy*Psize Below: " + getDYPSIZE_BELOW());
		
		System.out.println("Dx*Psize Above: " + getDXPSIZE_ABOVE());
		System.out.println("Dy*Psize Above: " + getDYPSIZE_ABOVE());
		
		System.out.println("Cetroid distance: " + getCENTER_RESOLUTION());
	}
}

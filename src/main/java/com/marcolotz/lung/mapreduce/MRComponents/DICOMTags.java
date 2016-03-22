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

package com.marcolotz.lung.mapreduce.MRComponents;

/**
 * This class contains all the DICOM tags address used by this application
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public abstract class DICOMTags {

	public static final String SOPClassUID = "0008,0016";

	// Unique identifier for the image.
	public static final String SOPInstanceUID = "0008,0018";

	public static final String StudyDate = "0008,0020";
	public static final String SeriesDate = "0008,0021";
	public static final String ImageDate = "0008,0023";

	public static final String StudyTime = "0008,0030";
	public static final String SeriesTime = "0008,0031";
	public static final String ImageTime = "0008,0033";

	public static final String Modality = "0008,0060";

	public static final String Manufacturer = "0008,0070";
	public static final String InstitutionName = "0008,0080";
	public static final String InstitutionAddress = "0008,0081";

	public static final String StationName = "0008,1010";
	public static final String StudyDescription = "0008,1030";

	public static final String PatientsName = "0010,0010";
	public static final String PatientsID = "0010,0020";

	public static final String BodyPartExamined = "0018,0015";
	public static final String SliceThickness = "0018,0050";
	public static final String KVP = "0018,0060";
	public static final String SpaceBetweenSlices = "0018,0088";

	public static final String AquisitionNumber = "0020,0012";
	public static final String ImageNumber = "0020,0013";

	/*
	 * The position (three coordinates) of the upper left corner of the image,
	 * relative to the patient???s coordinate system.
	 */
	public static final String ImagePosition = "0020,0032";

	/*
	 * Row and column vectors (six coordinates total) describing the orientation
	 * of the image.
	 */
	public static final String ImageOrientation = "0020,0037";

	// Unique identifier for the series that the image belongs to.
	// I.e. Files in different folder belong to different exams.
	public static final String SeriesInstanceUID = "0020,000E";

	// The size of a pixel (in mm).
	public static final String PixelSpacing = "0028,0030";

}

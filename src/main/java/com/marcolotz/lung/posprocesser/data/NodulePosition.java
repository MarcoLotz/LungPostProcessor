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

import com.marcolotz.lung.mapreduce.MapperComponents.ImageMetadata;
import com.marcolotz.lung.mapreduce.MapperComponents.MetaNodesCandidates;

/**
 * Used to find out what nodule candidates (and what image those candidates come
 * from) compose the current nodule.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class NodulePosition {

	/***
	 * SOP of image where this nodule position came from
	 */
	String SOPInstanceUID;

	/***
	 * Time and date information for the image.
	 */
	String imageDate;
	String imageTime;

	/***
	 * Number of that image in the whole series.
	 */
	int imageNumber;

	/***
	 * Image position in x, y and z taking into consideration the patients
	 * referential for DICOM images.
	 */
	String imagePosition;

	/***
	 * The x and y coordinates of the top left of the bounding box around the
	 * nodule candidate.
	 */
	int xcoord;
	int ycoord;

	/***
	 * The z coordinate of the slice where the node segment can be found.
	 */
	float zcoord;

	public NodulePosition(MetaNodesCandidates node, ImageMetadata image) {
		this.xcoord = node.getxCoord();
		this.ycoord = node.getyCoord();

		this.SOPInstanceUID = image.getSOPInstanceUID();

		this.imageDate = image.getImageDate();
		this.imageTime = image.getImageTime();

		/* Remember that the Image Number has a space before the number */
		this.imageNumber = Integer.parseInt(image.getImageNumber().replaceAll(
				"\\s", ""));

		zcoord = calculateZCoordinate(image.getImagePosition());
	}

	/***
	 * The Z coordinate of the image, and thus from the nodule that is here
	 * described. It comes from the imagePosition tag of the DICOM image.
	 * 
	 * @param position
	 * @return The Z coordinate of the image
	 */
	private float calculateZCoordinate(String position) {
		/* the format is divided in x coordinate // y coordinate // z coordinate */
		String parts[] = position.split("\\\\");

		return Float.parseFloat(parts[2]);
	}
}

/*******************************************************************************
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * 
 * Copyright (c) 2014 Marco Aurelio Barbosa Fagnani Gomes Lotz (marcolotz.com)
 * 
 * The source code in this document is licensed under Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License. You must 
 * credit the author of the source code in the way specified by the author or
 * licenser (but not in a way to suggest that the author or licenser has given 
 * you allowance to you or to your use of the source code). If you modify,
 * transform or create using this source code as basis, you can only distribute
 * the new source code under the same license or a similar license to this one.
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * To see a copy of the license, access:
 * creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 ******************************************************************************/
package com.marcolotz.lung.posprocesser.data;

import com.marcolotz.MapperComponents.ImageMetadata;
import com.marcolotz.MapperComponents.MetaNodesCandidates;

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

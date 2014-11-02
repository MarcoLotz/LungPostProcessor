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
package com.marcolotz.MapperComponents;

import ij.blob.Blob;
import ij.plugin.DICOM;
import ij.util.DicomTools;

import java.util.ArrayList;

import com.marcolotz.MRComponents.DICOMTags;

/**
 * The object contains a list of the nodes candidates for a single image. It
 * also contains some meta information about the image. Remember that, although
 * it contains metadata about the image, it is of paramount importance that it
 * does not contain the image itself, to avoid overhead. It is latter converted
 * to the mapper value: the ImageMetaData information.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ImageStructure {

	// * =================Individual Image Data==================== */

	// Unique identifier for the image.
	private String SOPInstanceUID;

	private String imageDate;
	private String imageTime;

	private String aquisitionNumber;
	private String imageNumber;

	/*
	 * The position (three coordinates) of the upper left corner of the image,
	 * relative to the patient's coordinate system.
	 */
	private String imagePosition;

	/*
	 * Row and column vectors (six coordinates total) describing the orientation
	 * of the image.
	 */
	private String imageOrientation;

	// * ========================================================== */

	private ArrayList<Blob> nodeCandidatesList;

	public ImageStructure(DICOM image) {
		generateMetadata(image);
	}

	/***
	 * Used for adding the nodes candidates of the read image to the object
	 * 
	 * @param candidates
	 */
	public void setNodeCandidatesList(ArrayList<Blob> candidates) {
		this.nodeCandidatesList = candidates;
	}

	public ArrayList<Blob> getNodeCandidatesList() {
		return this.nodeCandidatesList;
	}

	/**
	 * @return the SOPInstanceUID
	 */
	public String getSOPInstanceUID() {
		return SOPInstanceUID;
	}

	/**
	 * @return the imageDate
	 */
	public String getImageDate() {
		return imageDate;
	}

	/**
	 * @return the imageTime
	 */
	public String getImageTime() {
		return imageTime;
	}

	/**
	 * @return the aquisitionNumber
	 */
	public String getAquisitionNumber() {
		return aquisitionNumber;
	}

	/**
	 * @return the imageNumber
	 */
	public String getImageNumber() {
		return imageNumber;
	}

	/**
	 * @return the imagePosition
	 */
	public String getImagePosition() {
		return imagePosition;
	}

	/**
	 * @return the imageOrientation
	 */
	public String getImageOrientation() {
		return imageOrientation;
	}

	/***
	 * Generates all the meta data part of the Value. Keep in mind that it also
	 * need to get the nodes candidates from the processing part.
	 * 
	 * @param DICOM
	 *            image
	 */
	private void generateMetadata(DICOM image) {
		SOPInstanceUID = DicomTools.getTag(image, DICOMTags.SOPInstanceUID);

		imageDate = DicomTools.getTag(image, DICOMTags.ImageDate);
		imageTime = DicomTools.getTag(image, DICOMTags.ImageTime);

		aquisitionNumber = DicomTools.getTag(image, DICOMTags.AquisitionNumber);

		imageNumber = DicomTools.getTag(image, DICOMTags.ImageNumber);
		imagePosition = DicomTools.getTag(image, DICOMTags.ImagePosition);
		imageOrientation = DicomTools.getTag(image, DICOMTags.ImageOrientation);
	}
}

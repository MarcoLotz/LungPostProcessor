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

package com.marcolotz.lung.mapreduce.MapperComponents;

import ij.blob.Blob;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.Writable;

import com.marcolotz.lung.mapreduce.MRComponents.SerializerConverter;

/**
 * After the mapping process, there is no need to keep the Blob itself in image
 * processed object, just a few of its attributes. This class is used in the
 * Reduce phase has a simple representation of the processed image.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ImageMetadata implements Writable, Comparable<ImageMetadata> {

	// * =================Individual Image Data==================== */

	// Unique identifier for the image.
	private String SOPInstanceUID;

	private String imageDate;
	private String imageTime;

	private String aquisitionNumber;
	private String imageNumber;

	/*
	 * The position (three coordinates) of the upper left corner of the image,
	 * relative to the patients coordinate system.
	 */
	private String imagePosition;

	/*
	 * Row and column vectors (six coordinates total) describing the orientation
	 * of the image.
	 */
	private String imageOrientation;

	ArrayList<MetaNodesCandidates> CandidatesList = new ArrayList<MetaNodesCandidates>();

	// * ========================================================== */

	public ImageMetadata() {
	}

	public ImageMetadata(ImageMetadata source) {
		this.SOPInstanceUID = source.getSOPInstanceUID();

		this.imageDate = source.getImageDate();
		this.imageTime = source.getImageTime();

		this.aquisitionNumber = source.getAquisitionNumber();
		this.imageNumber = source.getImageNumber();

		this.imagePosition = source.getImagePosition();
		this.imageOrientation = source.getImageOrientation();

		CandidatesList = new ArrayList<MetaNodesCandidates>(source
				.getBlobMetaList().size());

		for (MetaNodesCandidates nodeCandidate : source.getBlobMetaList()) {
			CandidatesList.add(new MetaNodesCandidates(nodeCandidate));
		}
	}

	public ImageMetadata(ImageStructure mappedValue) {
		this.SOPInstanceUID = mappedValue.getSOPInstanceUID();

		this.imageDate = mappedValue.getImageDate();
		this.imageTime = mappedValue.getImageTime();

		this.aquisitionNumber = mappedValue.getAquisitionNumber();
		this.imageNumber = mappedValue.getImageNumber();

		this.imagePosition = mappedValue.getImagePosition();
		this.imageOrientation = mappedValue.getImageOrientation();

		CandidatesList = new ArrayList<MetaNodesCandidates>();

		getBlobMetaInfo(mappedValue);
	}

	private void getBlobMetaInfo(ImageStructure mappedValue) {
		ArrayList<Blob> blobList = mappedValue.getNodeCandidatesList();

		Iterator<Blob> iterator = blobList.iterator();

		while (iterator.hasNext()) {
			MetaNodesCandidates metab = new MetaNodesCandidates(iterator.next());
			CandidatesList.add(metab);
		}

	}

	/**
	 * @return the sOPInstanceUID
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

	/**
	 * @return the blobMetaList
	 */
	public ArrayList<MetaNodesCandidates> getBlobMetaList() {
		return CandidatesList;
	}

	@Override
	public void write(DataOutput out) throws IOException {

		SerializerConverter.writeString(SOPInstanceUID, out);

		SerializerConverter.writeString(imageDate, out);
		SerializerConverter.writeString(imageTime, out);

		SerializerConverter.writeString(aquisitionNumber, out);
		SerializerConverter.writeString(imageNumber, out);

		SerializerConverter.writeString(imagePosition, out);

		SerializerConverter.writeString(imageOrientation, out);

		/* Serializes the array list */
		// Writes the size of the list
		SerializerConverter.writeInt(CandidatesList.size(), out);
		// Writes the contents of the list
		for (MetaNodesCandidates candidates : CandidatesList) {
			candidates.write(out);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		SOPInstanceUID = SerializerConverter.readString(in);

		imageDate = SerializerConverter.readString(in);
		imageTime = SerializerConverter.readString(in);

		aquisitionNumber = SerializerConverter.readString(in);
		imageNumber = SerializerConverter.readString(in);

		imagePosition = SerializerConverter.readString(in);

		imageOrientation = SerializerConverter.readString(in);

		/* de Serializes the array list */
		// reads the size of the list:
		int listSize = SerializerConverter.readInt(in);

		// Allocates a new list:
		this.CandidatesList = new ArrayList<MetaNodesCandidates>(listSize);

		// populate the list:
		for (int i = 0; i < listSize; i++) {
			MetaNodesCandidates nodeCandidate = new MetaNodesCandidates();
			nodeCandidate.readFields(in);
			CandidatesList.add(nodeCandidate);
		}
	}

	/***
	 * Compares two imageMetadata taking as base the imageNumber. It is going to
	 * sort from the lower to the higher. A negative integer is returned if this
	 * object is lesser than the comparedImage. Zero if they are equal and a
	 * positive integer if this object is greater than the comparedImage.
	 */
	@Override
	public int compareTo(ImageMetadata comparedImage) {

		/*
		 * Removes all spaces and non-visible characters, like tabs from the
		 * string
		 */
		String tmpNumberHere = this.imageNumber.replaceAll("\\s+", "");
		String tmpNumberComparedImage = comparedImage.imageNumber.replaceAll(
				"\\s+", "");

		int imageNumberHere = Integer.parseInt(tmpNumberHere);
		int imageNumberComparedImage = Integer.parseInt(tmpNumberComparedImage);

		if (imageNumberHere == imageNumberComparedImage) {
			return 0;
		} else {
			if (imageNumberHere > imageNumberComparedImage) {
				return +1;
			} else {
				return -1;
			}
		}
	}

}

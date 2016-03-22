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

import java.util.ArrayList;
import java.util.Iterator;

import com.marcolotz.lung.mapreduce.MapperComponents.ImageMetadata;
import com.marcolotz.lung.mapreduce.MapperComponents.MetaNodesCandidates;
import com.marcolotz.lung.mapreduce.io.outputFormat.SeriesDataWritable;


/**
 * The overall results and composition of a post-processed series.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class SeriesResults {

	/***
	 * DICOM image tags:
	 */
	String seriesInstanceUID;

	String seriesDate;
	String seriesTime;

	/***
	 * The width of a pixel in millimeters for that series.
	 */
	private float pixelWidth;

	/***
	 * The height of a pixel in millimeters for that series.
	 */
	private float pixelHeight;

	/***
	 * The depth of a voxel in millimeters. The depth is considered by the z
	 * distance between two slides.
	 */
	private float voxelDepth;

	/***
	 * By default no series has nodules.
	 */
	private boolean hasNodules = false;

	/***
	 * List of all nodules found in this series.
	 */
	private ArrayList<Nodule> nodulesList;

	public SeriesResults(SeriesDataWritable series) {

		seriesInstanceUID = series.getKeyStructureWritable()
				.getSeriesInstanceUID();

		seriesDate = series.getKeyStructureWritable().getSeriesDate();
		seriesTime = series.getKeyStructureWritable().getSeriesTime();

		generatePixelResolution(series);
		generateVoxelDepth(series);

		nodulesList = new ArrayList<Nodule>();
	}

	/***
	 * Extract the pixel resolution for that series.
	 * 
	 * @param series
	 */
	private void generatePixelResolution(SeriesDataWritable series) {
		String pixelInfo = series.getKeyStructureWritable().getPixelSpacing();

		/* the format is: width // height */
		String parts[] = pixelInfo.split("\\\\");

		pixelWidth = Float.parseFloat(parts[0]);
		pixelHeight = Float.parseFloat(parts[1]);
	}

	/***
	 * It will get the voxel depth from slice thickness of DICOM series. Another
	 * approach is to get is from the image list, checking the distance between
	 * two images. I used the first because same series have non-equally spaced
	 * set of images.
	 * 
	 * @param series
	 */
	private void generateVoxelDepth(SeriesDataWritable series) {
		this.voxelDepth = Float.parseFloat(series.getKeyStructureWritable()
				.getSliceThickness());
	}

	/***
	 * Processes the whole series, looking for nodules. Uses the correlational
	 * method for detecting nodules. By this, it checks if two nodule candidates
	 * in adjacent slices may actually be part of the same nodule.
	 * 
	 * @param series
	 */
	public void process(SeriesDataWritable series) {

		/**
		 * All the images in that series.
		 */
		ArrayList<ImageMetadata> basisImageList = series.getReducedValue()
				.getReducedList();

		// Total number of images in that series
		int numberOfImages = basisImageList.size();

		/*
		 * the verification only has a meaning if the series has more than just
		 * one image.
		 */
		if (numberOfImages > 1) {
			/* Index of ImageMetadata */
			int basisCounter = 0;

			/* only iterates until the penultimate image */
			for (basisCounter = 0; basisCounter < numberOfImages - 1; basisCounter++) {

				/* Gets the node list in the basis image */
				Iterator<MetaNodesCandidates> bCandidatesItr = basisImageList
						.get(basisCounter).getBlobMetaList().iterator();

				/* While there are nodules candidates in that slice */
				while (bCandidatesItr.hasNext()) {
					/*
					 * Verifies Correlation in the next slices for each of the
					 * nodules candidates in the basis image
					 */
					verifyCorrelation(bCandidatesItr.next(), basisImageList,
							basisCounter);
				}
			}
		}
	}

	/***
	 * Verify if two nodule candidates, from adjacent slices, are the same
	 * nodule using the 3D correlation criterium. A range for the centroid is
	 * defined. If the two nodules candidates centroid are within the range,
	 * they are the same nodule.
	 * 
	 * @param currentCandidate
	 * @param basisImageList
	 * @param baseIndex
	 */
	private void verifyCorrelation(MetaNodesCandidates currentCandidate,
			ArrayList<ImageMetadata> basisImageList, int baseIndex) {

		boolean nodeFound = false;
		/*
		 * Generates a node information. It will be thrown away if no other
		 * candidates match it in a 3D analysis
		 */

		/* Starts to analyse in the image right after the basis */
		int forwardIndex = baseIndex + 1;

		/* Gets the next image */
		ImageMetadata forwardImage = basisImageList.get(forwardIndex);

		/* Generates the temporary nodule */
		Nodule currentNodule = new Nodule(this, currentCandidate, forwardImage);

		/* Gets the nodules candidates in the forward image */
		Iterator<MetaNodesCandidates> forwardNodulesItr = forwardImage
				.getBlobMetaList().iterator();

		/* Runs for each candidate in the forward image */
		while (forwardNodulesItr.hasNext()) {
			MetaNodesCandidates forwardCandidate = forwardNodulesItr.next();

			/* If they match the correlation criterium */
			if (checkCorrelation(currentCandidate, forwardCandidate)) {
				/*
				 * If they match the check Correlation criteria, updates the
				 * nodule information
				 */
				currentNodule.updateNoduleInfo(forwardCandidate, forwardImage);

				/*
				 * Removes that nodule from the Forward Image Candidates List,
				 * so it won't be processed twice, when that image becomes the
				 * basis.
				 */
				forwardNodulesItr.remove();

				nodeFound = true;

				/*
				 * Go to the next image if the forward image isnt already the
				 * last one. The index size-1 is last index of the array. All
				 * the other nodes in that forward image may be ignored, because
				 * one already matched.
				 */
				if (forwardIndex < basisImageList.size() - 1) {
					forwardIndex++;
					forwardImage = basisImageList.get(forwardIndex);
				}
				/* Gets a new list of candidates */
				forwardNodulesItr = forwardImage.getBlobMetaList().iterator();
			}
		}

		if (nodeFound) {
			this.nodulesList.add(currentNodule);
		}
	}

	/***
	 * Uses centroid distance criteria to verify if two nodules candidates, in
	 * adjacent images, are the same nodule.
	 * 
	 * @param currentBasisCandidate
	 * @param currentForwardCandidate
	 * @return true if they in the object by this criteria. 
	 */
	private boolean checkCorrelation(MetaNodesCandidates currentBasisCandidate,
			MetaNodesCandidates currentForwardCandidate) {

		double node1XCenter = currentBasisCandidate.getxCoord()
				+ (currentBasisCandidate.getWidth() / 2);
		double node1YCenter = currentBasisCandidate.getyCoord()
				+ (currentBasisCandidate.getHeight() / 2);

		double node2XCenter = currentForwardCandidate.getxCoord()
				+ (currentForwardCandidate.getWidth() / 2);
		double node2YCenter = currentForwardCandidate.getyCoord()
				+ (currentForwardCandidate.getHeight() / 2);

		/* Centers are in the defined bounds for being the same nodule */
		if ((Math.abs(node1XCenter - node2XCenter) <= ReferencesParameters.CENTER_RESOLUTION)
				&& (Math.abs(node1YCenter - node2YCenter) <= ReferencesParameters.CENTER_RESOLUTION)) {
			return true;
		}
		return false;
	}

	/***
	 * Apply the false positive parameters (R1, R2, R3) on all currently
	 * detected nodules.
	 */
	public void validateNodules() {
		Iterator<Nodule> itr = nodulesList.iterator();

		while (itr.hasNext()) {
			/* At least a node has been detected in the exam */
			if (itr.next().verifyNode() == true) {
				this.hasNodules = true;
			} else {
				/* if it is not a nodule, remove it from the nodule list. */
				itr.remove();
			}
		}
	}

	/**
	 * @return the seriesInstanceUID
	 */
	public String getSeriesInstanceUID() {
		return seriesInstanceUID;
	}

	/**
	 * @param seriesInstanceUID
	 *            the seriesInstanceUID to set
	 */
	public void setSeriesInstanceUID(String seriesInstanceUID) {
		this.seriesInstanceUID = seriesInstanceUID;
	}

	/**
	 * @return the seriesDate
	 */
	public String getSeriesDate() {
		return seriesDate;
	}

	/**
	 * @param seriesDate
	 *            the seriesDate to set
	 */
	public void setSeriesDate(String seriesDate) {
		this.seriesDate = seriesDate;
	}

	/**
	 * @return the seriesTime
	 */
	public String getSeriesTime() {
		return seriesTime;
	}

	/**
	 * @param seriesTime
	 *            the seriesTime to set
	 */
	public void setSeriesTime(String seriesTime) {
		this.seriesTime = seriesTime;
	}

	/**
	 * @return the pixelWidth
	 */
	public float getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * @param pixelWidth
	 *            the pixelWidth to set
	 */
	public void setPixelWidth(float pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	/**
	 * @return the pixelHeight
	 */
	public float getPixelHeight() {
		return pixelHeight;
	}

	/**
	 * @param pixelHeight
	 *            the pixelHeight to set
	 */
	public void setPixelHeight(float pixelHeight) {
		this.pixelHeight = pixelHeight;
	}

	/**
	 * @return the voxelDepth
	 */
	public float getVoxelDepth() {
		return voxelDepth;
	}

	/**
	 * @param voxelDepth
	 *            the voxelDepth to set
	 */
	public void setVoxelDepth(float voxelDepth) {
		this.voxelDepth = voxelDepth;
	}

	/**
	 * @return the hasNodules
	 */
	public boolean hasNodules() {
		return hasNodules;
	}

	/**
	 * @param hasNodules
	 *            the hasNodules to set
	 */
	public void setHasNodules(boolean hasNodules) {
		this.hasNodules = hasNodules;
	}

	/**
	 * @return the nodulesList
	 */
	public ArrayList<Nodule> getNodulesList() {
		return nodulesList;
	}

	/**
	 * @param nodulesList
	 *            the nodulesList to set
	 */
	public void setNodulesList(ArrayList<Nodule> nodulesList) {
		this.nodulesList = nodulesList;
	}

}

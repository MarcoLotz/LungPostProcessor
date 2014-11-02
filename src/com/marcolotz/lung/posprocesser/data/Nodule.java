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

import java.util.ArrayList;

import com.marcolotz.MapperComponents.ImageMetadata;
import com.marcolotz.MapperComponents.MetaNodesCandidates;

/**
 * Describes a Lung Nodule.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class Nodule {

	/***
	 * R1, R2 and R3 parameters from the Local Maximum Density paper.
	 */
	float r1Parameter = 0;
	float r2Parameter = 0;
	float r3Parameter = 0;

	/***
	 * Width in pixels
	 */
	int dx = 0;

	/***
	 * Height in pixels
	 */
	int dy = 0;

	/***
	 * Z depth in pixels
	 */
	int dz = 0;

	/*** Calculated using the enclosed area of the node multiplied by its depth */
	int numberOfObjectVoxels = 0;

	/***
	 * The maximum width of the nodule, in millimeters.
	 */
	float maximumWidth = 0;

	/***
	 * The maximum height of the nodule, in millimeters.
	 */
	float maximumHeight = 0;

	/***
	 * The maximum Elongation, which means the maximum z length of it, in
	 * millimeters.
	 */
	float maximumElongation = 0;

	/***
	 * The positions where that nodule is present.
	 */
	ArrayList<NodulePosition> positions;

	/***
	 * Has passed the verification test, using the R parameters.
	 */
	boolean isTrullyNode = false;

	/***
	 * Pixel size in millimeters.
	 */
	double pSizeX;
	double pSizeY;
	double pSizeZ;

	/***
	 * Number of slices that a given nodule appears
	 */
	int numberOfSlices = 1;

	/***
	 * Default Constructor. Generates a new nodule, get series pixel information
	 * and the image where that nodule appears, so it can populate the locations
	 * list.
	 * 
	 * @param series
	 * @param node
	 * @param image
	 */
	public Nodule(SeriesResults series, MetaNodesCandidates node,
			ImageMetadata image) {
		/* Get the pixel size in mm */
		pSizeX = series.getPixelWidth();
		pSizeY = series.getPixelHeight();
		pSizeZ = series.getVoxelDepth();

		/* The maximum values are currently just that NoduleCandidate dimensions */
		dx = (int) node.getWidth();
		dy = (int) node.getHeight();
		dz = 1;

		/*
		 * Since I cannot say exactly how many pixels are in the slice, I will
		 * use the getEnclosedArea to give and approximation of the area itself.
		 * The result will be ceiled.
		 */
		numberOfObjectVoxels = (int) Math.ceil((node.getEnclosedArea() * dz));

		/* Finds maximum values in millimeters */
		maximumWidth = (float) (dx * pSizeX);
		maximumHeight = (float) (dy * pSizeY);
		maximumElongation = (float) (dz * pSizeZ);

		positions = new ArrayList<NodulePosition>();
		positions.add(new NodulePosition(node, image));
	}

	/***
	 * R1 is defined as the ratio of the volume of the object to the volume of a
	 * modified bounding box of the object.
	 * 
	 * From the formula:
	 * 
	 * R1 = number of object voxels/(max(dx,dy)*max(dx,dy)*dz)
	 */
	void calculateR1() {
		float numerator = numberOfObjectVoxels;
		float divisor = (Math.max(dx, dy) * Math.max(dx, dy) * dz);

		this.r1Parameter = numerator / divisor;
	}

	/***
	 * R2 is defined as the ratio of the maximal projection length of the object
	 * along the axis of z to the maximal projection length of the object along
	 * the axis of x or y, whichever is larger. The non-isotropic
	 * characteristics of CT scan are taken into account by multiplying the
	 * length 'in pixel' with the corresponding pixel size 'in mm'. In this
	 * application, the 'maximum' attributes are already in millimeters, so no
	 * multiplication was required.
	 * 
	 * From the formula: R2 = dz*(p-size z)/(max(dx,dy)*(p-size x)
	 */
	void calculateR2() {
		float numerator = (float) (dz * pSizeZ);
		float divisor = (float) (Math.max(dx, dy) * (pSizeX));
		this.r2Parameter = numerator / divisor;
	}

	/***
	 * R3 is defined as the ratio of the maximal projection length of the object
	 * along the axis of x or y, whichever is larger to the maximal projection
	 * length of the object along the axis of x or y, whichever is smaller.
	 * 
	 * From the formulae: R3 = max(dx,dy)/min(dx,dy)
	 */
	void calculateR3() {
		float numerator = Math.max(dx, dy);
		float divisor = Math.min(dx, dy);

		this.r3Parameter = numerator / divisor;
	}

	/***
	 * Once all nodes are calculates by the 3D correlational analysis, apply the
	 * parameters on each one of them. If they fail the test, they should be
	 * removed from the Nodule base.
	 */
	public boolean verifyNode() {
		calculateR1();
		calculateR2();
		calculateR3();

		if ((r1Parameter < ReferencesParameters.getREFERENCE_R1_MAXIMUM())
				|| (r2Parameter > ReferencesParameters.getREFERENCE_R2_ABOVE())
				|| (r2Parameter < ReferencesParameters.getREFERENCE_R2_BELOW())
				|| (r3Parameter > ReferencesParameters.getREFERENCE_R3_MINIMUM())
				||

				((dx * pSizeX < ReferencesParameters.getDXPSIZE_BELOW()) && (dy
						* pSizeY < ReferencesParameters.getDYPSIZE_BELOW()))
				|| ((dx * pSizeX > ReferencesParameters.getDXPSIZE_ABOVE()) && (dy
						* pSizeY > ReferencesParameters.getDYPSIZE_ABOVE()))) {
			isTrullyNode = false;
		} else {
			isTrullyNode = true;
		}

		return isTrullyNode;
	}

	/***
	 * Updates the nodule info, adding new candidates that matched that node in
	 * the correlational analysis to the nodule information.
	 * 
	 * @param node
	 * @param image
	 */
	public void updateNoduleInfo(MetaNodesCandidates node, ImageMetadata image) {

		/* That nodule appears in another slice */
		numberOfSlices++;

		/* Generates the position description for that node */
		NodulePosition position = new NodulePosition(node, image);

		/* Were the dx, dy and dz values updated? */
		boolean dValuresUpdated = false;

		if (node.getWidth() > this.dx) {
			dx = (int) node.getWidth();
			dValuresUpdated = true;
		}

		if (node.getHeight() > this.dy) {
			dy = (int) node.getHeight();
			dValuresUpdated = true;
		}

		/* If so, recalculates the maximum attributes */
		if (dValuresUpdated) {
			maximumWidth = (float) (dx * pSizeX);
			maximumHeight = (float) (dy * pSizeY);
		}

		/* Updates the number of occupied Object voxels */
		numberOfObjectVoxels = (int) (numberOfObjectVoxels + (node
				.getEnclosedArea() * 1));

		/* The updates its depth (in pixels) for the z axis */
		dz++;
		maximumElongation = (float) (dz * pSizeZ);

		/* add the image to the position where the node appears */
		positions.add(position);
	}

}

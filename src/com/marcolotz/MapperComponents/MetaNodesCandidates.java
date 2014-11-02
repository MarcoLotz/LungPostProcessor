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

import java.awt.Polygon;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.marcolotz.MRComponents.SerializerConverter;

import ij.blob.Blob;

/**
 * This class holds the important metadata from the blob generated in the Map
 * phase, in order to make the JSON serialization possible.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class MetaNodesCandidates implements Writable, Cloneable {

	/* Value of Bounding Box around the Outer Contour */
	double width;
	double height;

	/* X and Y coordinates of the top left corner of the bounding box */
	int xCoord;
	int yCoord;

	/**
	 * Is the circularity of the outer contour: (perimeter*perimeter) /
	 * (enclosed area). If the value approaches 0.0, it indicates that the
	 * polygon is increasingly elongated.
	 * 
	 */
	double circularity;

	double enclosedArea;
	double perimeter;

	/***
	 * Used only for reading from the stream in the ImetaMetadata.
	 */
	public MetaNodesCandidates() {
	}

	public MetaNodesCandidates(Blob blob) {
		this.circularity = blob.getCircularity();
		this.enclosedArea = blob.getEnclosedArea();
		this.perimeter = blob.getPerimeter();

		Polygon pol = blob.getOuterContour();

		/* A pixel of width is considered 0 by the algorithm */
		this.width = pol.getBounds().width;
		this.height = pol.getBounds().height;

		this.width++;
		this.height++;

		this.xCoord = pol.getBounds().x;
		this.yCoord = pol.getBounds().y;
	}

	/***
	 * Implemented in order to generate a list in the reduce phase. Otherwise
	 * the list contains several times the same element. Actually it clones the
	 * source content.
	 */
	public MetaNodesCandidates(MetaNodesCandidates source) {
		this.circularity = source.getCircularity();
		this.enclosedArea = source.getEnclosedArea();
		this.perimeter = source.getPerimeter();

		this.width = source.getWidth();
		this.height = source.getHeight();

		this.xCoord = source.getxCoord();
		this.yCoord = source.getyCoord();
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * @return the yCoord
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * @return the circularity
	 */
	public double getCircularity() {
		return circularity;
	}

	/**
	 * @return the enclosedArea
	 */
	public double getEnclosedArea() {
		return enclosedArea;
	}

	/**
	 * @return the perimeter
	 */
	public double getPerimeter() {
		return perimeter;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		SerializerConverter.writeDouble(circularity, out);

		SerializerConverter.writeDouble(enclosedArea, out);
		SerializerConverter.writeDouble(perimeter, out);

		SerializerConverter.writeDouble(width, out);
		SerializerConverter.writeDouble(height, out);

		SerializerConverter.writeInt(xCoord, out);
		SerializerConverter.writeInt(yCoord, out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		circularity = SerializerConverter.readDouble(in);

		enclosedArea = SerializerConverter.readDouble(in);
		perimeter = SerializerConverter.readDouble(in);

		width = SerializerConverter.readDouble(in);
		height = SerializerConverter.readDouble(in);

		xCoord = SerializerConverter.readInt(in);
		yCoord = SerializerConverter.readInt(in);
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @param xCoord
	 *            the xCoord to set
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @param yCoord
	 *            the yCoord to set
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	/**
	 * @param circularity
	 *            the circularity to set
	 */
	public void setCircularity(double circularity) {
		this.circularity = circularity;
	}

	/**
	 * @param enclosedArea
	 *            the enclosedArea to set
	 */
	public void setEnclosedArea(double enclosedArea) {
		this.enclosedArea = enclosedArea;
	}

	/**
	 * @param perimeter
	 *            the perimeter to set
	 */
	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}
}

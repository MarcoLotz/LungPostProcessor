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

import java.awt.Polygon;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.marcolotz.lung.mapreduce.MRComponents.SerializerConverter;

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

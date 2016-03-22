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
package com.marcolotz.lung.mapreduce.ReducerComponents;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Writable;

import com.marcolotz.lung.mapreduce.MRComponents.SerializerConverter;
import com.marcolotz.lung.mapreduce.MapperComponents.ImageMetadata;


/**
 * In this application one generates a list of MappedValueKeys that will be
 * later serialized into JSON format. The Key Value will be the same for the
 * mapper and for the reducer, since its data should not change if the images
 * are in the same series.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ReducedValueWritable implements Writable {

	private ArrayList<ImageMetadata> reducedList = new ArrayList<ImageMetadata>();

	public ReducedValueWritable() {
	}

	public ReducedValueWritable(ImageMetadata mvs) {
		this.addToReducedList(mvs);
	}

	public ArrayList<ImageMetadata> getReducedList() {
		return this.reducedList;
	}

	public void addToReducedList(ImageMetadata mvs) {
		reducedList.add(mvs);
	}

	@Override
	public void write(DataOutput out) throws IOException {

		/* Serializes the array list */
		// Writes the size of the list
		SerializerConverter.writeInt(reducedList.size(), out);
		// Writes the contents of the list
		for (ImageMetadata imgData : reducedList) {
			imgData.write(out);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		/*
		 * deserializes the array list // reads the size of the list:
		 */
		int listSize = SerializerConverter.readInt(in);

		// Allocates a new list:
		this.reducedList = new ArrayList<ImageMetadata>(listSize);

		// populate the list:
		for (int i = 0; i < listSize; i++) {
			ImageMetadata imgData = new ImageMetadata();
			imgData.readFields(in);
			reducedList.add(imgData);
		}
	}
}

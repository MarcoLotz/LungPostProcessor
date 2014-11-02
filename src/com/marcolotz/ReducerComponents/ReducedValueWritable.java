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
package com.marcolotz.ReducerComponents;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Writable;

import com.marcolotz.MRComponents.SerializerConverter;
import com.marcolotz.MapperComponents.ImageMetadata;

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

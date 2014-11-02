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
package com.marcolotz.lung.io.outputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marcolotz.MRComponents.KeyStructureWritable;
import com.marcolotz.ReducerComponents.ReducedValueWritable;

/**
 * Contains all the meta information of a processed DICOM series. By series, one
 * can understand a group of images that were obtained in the same exam
 * procedure.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class SeriesDataWritable implements Writable {
	KeyStructureWritable KeyStructureWritable;
	ReducedValueWritable reducedValue;

	public SeriesDataWritable() {
	}

	public SeriesDataWritable(KeyStructureWritable ks,
			ReducedValueWritable rValue) {
		this.KeyStructureWritable = ks;
		this.reducedValue = rValue;
	}

	/**
	 * @return the KeyStructureWritable
	 */
	public KeyStructureWritable getKeyStructureWritable() {
		return KeyStructureWritable;
	}

	/**
	 * @return the reducedValue
	 */
	public ReducedValueWritable getReducedValue() {
		return reducedValue;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		KeyStructureWritable.write(out);
		reducedValue.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		KeyStructureWritable.readFields(in);
		reducedValue.readFields(in);
	}

	/***
	 * This method has to be implemented because of the outputFormat used. I am
	 * going to use the TextOutputFormat that calls the toString() method of the
	 * object for output.
	 * 
	 * @return the JSON string that represents this object.
	 */
	@Override
	public String toString() {
		// Creates a new Gson
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(this);
	}
}

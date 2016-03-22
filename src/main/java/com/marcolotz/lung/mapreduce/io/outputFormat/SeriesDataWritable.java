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

package com.marcolotz.lung.mapreduce.io.outputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marcolotz.lung.mapreduce.MRComponents.KeyStructureWritable;
import com.marcolotz.lung.mapreduce.ReducerComponents.ReducedValueWritable;

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

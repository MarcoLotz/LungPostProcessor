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

package com.marcolotz.lung.mapreduce.MRComponents;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * This class converts the original java structure into Hadoop Writable
 * structures and then handles its serializations and deserializatons.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class SerializerConverter {

	/***
	 * This is a refactored manner to serialize a String. Basically it transform
	 * it into a Text and then writes it in the DataOutput.
	 * 
	 * @param outputString
	 * @param out
	 * @throws IOException
	 */
	public static void writeString(String outputString, DataOutput out)
			throws IOException {
		Text writtenString;

		/* Prevents a null string exception when writting it to the output */
		if (outputString == null) {
			writtenString = new Text("null");
		} else {
			writtenString = new Text(outputString);
		}
		writtenString.write(out);
	}

	/***
	 * This is a refactored manner to deserialize a String. Basically it creates
	 * a new Text, reads the field and then return the new String;
	 * 
	 * @param dataInput
	 * @return the string that was readen
	 * @throws IOException
	 */
	public static String readString(DataInput dataInput) throws IOException {
		Text readenString = new Text();
		readenString.readFields(dataInput);
		return readenString.toString();
	}
	
	/***
	 * Writes a double to the output.
	 * @param outputDouble
	 * @param out
	 * @throws IOException
	 */
	public static void writeDouble(double outputDouble, DataOutput out) throws IOException
	{
		DoubleWritable writtenDouble = new DoubleWritable(outputDouble);
		writtenDouble.write(out);
	}
	
	/***
	 * Reads a double from the input
	 * @param datainput
	 * @return the double readen
	 * @throws IOException
	 */
	public static double readDouble(DataInput datainput) throws IOException
	{
		DoubleWritable readenDouble = new DoubleWritable();
		readenDouble.readFields(datainput);
		return readenDouble.get();
	}
	
	/**
	 * Writes an int to the output.
	 * @param outputInt
	 * @param out
	 * @throws IOException
	 */
	public static void writeInt(int outputInt, DataOutput out) throws IOException
	{
		IntWritable writtenInt = new IntWritable(outputInt);
		writtenInt.write(out);
	}
	
	/***
	 * Reads an Int from the input
	 * @param datainput
	 * @return the int readen
	 * @throws IOException
	 */
	public static int readInt(DataInput datainput) throws IOException
	{
		IntWritable readenInt = new IntWritable();
		readenInt.readFields(datainput);
		return readenInt.get();
	}

}

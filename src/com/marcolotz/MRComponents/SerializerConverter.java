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
package com.marcolotz.MRComponents;

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

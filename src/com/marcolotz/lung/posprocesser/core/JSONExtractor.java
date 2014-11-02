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
package com.marcolotz.lung.posprocesser.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.marcolotz.lung.io.outputFormat.SeriesDataWritable;

/**
 * This objects deals with an input in a non-stardard JSON format file (since
 * there will be many JSON files inside only one output) and retrieves each of
 * the original JSON files (that are processed DICOM series) from it. It returns
 * a series at a time until the end of the file
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class JSONExtractor {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(JSONExtractor.class);

	/***
	 * Path to the MapReduce json file.
	 */
	String inputPath;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	JsonStreamParser parser;

	SeriesDataWritable currentSeries;

	/***
	 * Number of series found so far.
	 */
	private int numberOfExtractedSeries = 0;

	public JSONExtractor(String inputPath) {
		this.inputPath = inputPath;
	}

	/***
	 * Make possible initializations for the object. In this case the only
	 * initialization is open the input file
	 */
	public void initialize() {

		openFile();
	}

	/***
	 * Opens the input File
	 */
	private void openFile() {

		try {
			parser = new JsonStreamParser(new FileReader(inputPath));
		} catch (FileNotFoundException e) {
			System.err.println("Error openning file.");
			e.printStackTrace();
		}
	}

	/***
	 * Unserializes the next JSON object and sets as a temporary element that
	 * will be returned by the getSeries() method. More references on:
	 * http://stackoverflow
	 * .com/questions/11253476/reading-multiple-elements-from-json-file
	 * 
	 * @throws IOException
	 */
	public boolean hasNext() {

		if (parser.hasNext()) {

			currentSeries = gson.fromJson(parser.next(),
					SeriesDataWritable.class);
			numberOfExtractedSeries++;
			return true;
		}
		return false;
	}

	public SeriesDataWritable getSeries() {
		return currentSeries;
	}

	/**
	 * @return the numberOfExtractedSeries
	 */
	public int getNumberOfExtractedSeries() {
		return numberOfExtractedSeries;
	}
}

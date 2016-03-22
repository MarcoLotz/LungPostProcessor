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
package com.marcolotz.lung.posprocesser.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.marcolotz.lung.mapreduce.io.outputFormat.SeriesDataWritable;

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

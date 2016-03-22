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
package com.marcolotz.lung.posprocesser.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.marcolotz.lung.posprocesser.data.ReferencesParameters;
import com.marcolotz.lung.posprocesser.debug.ResultVerifier;

/**
 * An object of this class outputs the verification results to a file. It is
 * used for batch processing of the post processing application, allowing that
 * the result content to be used by GNU plot later in order to produce a ROC
 * graphic. This is a script specific approach, since the batch script output
 * there results in the format "output[bottom threshold]_[top threshold].json
 * i.e. output110_130.json
 * 
 * More refenreces for the GNU plot input sintax at:
 * http://cliftonphua.blogspot.
 * com.br/2010/08/creating-f-measure-and-roc-curves-using.html
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ValidationPrinter {

	ResultVerifier verifier;
	String inputFile;
	String outputPath;
	File outputFile;
	BufferedWriter output;

	private int bottomThreshold;
	private int topThreshold;

	public ValidationPrinter(ResultVerifier rv, String inputPath,
			String outputPath) {
		this.outputPath = outputPath;
		verifier = rv;
		outputFile = new File(outputPath);

		generateThresholds(inputPath);
	}

	/***
	 * keep in mind that it uses the pattern for MapReduce output defined in
	 * this class description.
	 * 
	 * @param inputPath
	 */
	private void generateThresholds(String inputPath) {
		File inputFile = new File(inputPath);
		String fileName = inputFile.getName();

		/* Removes any character that is not a number from the input format */
		String limits = fileName.replaceAll("[^0-9_]", "");

		String[] thresholds = limits.split("_");

		bottomThreshold = Integer.parseInt(thresholds[0]);
		topThreshold = Integer.parseInt(thresholds[1]);

	}

	public void write() {
		try {
			output = new BufferedWriter(new FileWriter(outputFile, true));
		} catch (IOException e1) {
			System.err.println("Problem openning the file.");
			e1.printStackTrace();
		}

		String result = new String();

		if (!outputFile.isDirectory()) {

			result = result + bottomThreshold + "	" + topThreshold + "	"
					+ ReferencesParameters.CENTER_RESOLUTION + "	" + ReferencesParameters.getDXPSIZE_BELOW() + "	" +ReferencesParameters.getDXPSIZE_ABOVE() + "	";

			result = result + verifier.toFile();
			result = result + "\n";
		}
		try {
			output.write(result);
		} catch (IOException e) {
			System.err.println("Problem writting the output.");
			e.printStackTrace();
		}
		
		try {
			output.close();
		} catch (IOException e) {
			System.err.println("Problem closing the output.");
			e.printStackTrace();
		}
	}
}

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

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import com.marcolotz.lung.io.outputFormat.SeriesDataWritable;
import com.marcolotz.lung.posprocesser.data.ReferencesParameters;
import com.marcolotz.lung.posprocesser.debug.ResultVerifier;
import com.marcolotz.lung.posprocesser.output.ValidationPrinter;

/**
 * Main processor class, contains main object used in this application. Removes
 * a single JSON object from the input, process it, and then gives the results
 * to the @see com.marcolotz.lung.posprocesser.core.PatientManager . Once all is
 * done, serializes the processed data in a user defined way and validates its
 * content (optional).
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class LungPostProcesser {

	private static final Logger LOG = Logger.getLogger(LungPostProcesser.class);

	/***
	 * Input path for JSON file.
	 */
	String inputPath;

	/***
	 * Output path for final processing report.
	 */
	String outputPath;

	String extractedElement;

	JSONExtractor extractorProcessor;

	SeriesDataWritable currentSeries;

	SeriesProcessor seriesProcessor;

	private PatientManager patientManager;
	
	Configuration conf = new Configuration();

	/***
	 * object used to verify true positives and false positives.
	 */
	ResultVerifier verifier = new ResultVerifier();

	public LungPostProcesser() {
	}

	/***
	 * verifies if the arguments are correct.
	 * 
	 * @param args
	 * @return System status
	 */
	private int checkUsage(String args[]) {
		if ((args.length != 2) && (args.length != 5)) {
			// Print default usage:
			System.out
					.printf("Usage: %s [generic option] <input JSON path> <Output Path>\n",
							this.getClass().getSimpleName());
			System.out.println("or");
			System.out
			.printf("Usage: %s [generic option] <input JSON path> <Output Path> <centroid resolution> <minimumg width/height> <maximum width/height>\n",
					this.getClass().getSimpleName());
			System.out.println("The candidate, to be a node, must be inside those values.");
			return -1;
		}

		inputPath = args[0];
		outputPath = args[1];
		
		/*TODO: in the future, make the user inform the attribute and the value on the command line instead */
		if ((args.length == 5))
		{
			ReferencesParameters.getConf().setDouble("com.marcolotz.nodule.centroidDistance", Double.parseDouble(args[2]));
			
			ReferencesParameters.getConf().setDouble("com.marcolotz.nodule.dxPsizeBelow", Double.parseDouble(args[3]));
			ReferencesParameters.getConf().setDouble("com.marcolotz.nodule.dyPsizeBelow", Double.parseDouble(args[3]));
			
			ReferencesParameters.getConf().setDouble("com.marcolotz.nodule.dxPsizeAbove", Double.parseDouble(args[4]));
			ReferencesParameters.getConf().setDouble("com.marcolotz.nodule.dyPsizeAbove", Double.parseDouble(args[4]));
			ReferencesParameters.updateReferences();
		}

		return 0;
	}

	/***
	 * Main method for execution
	 * 
	 * @param args
	 */
	public void run(String[] args) {
		if (checkUsage(args) != 0) {
			throw new IllegalArgumentException("Wrong arguments format.");
		}

		extractAndProcess();
	}

	/***
	 * Initializes the extraction and processing variables. Extracts and process
	 * one element a time. This is done due to possible memory overflow by a
	 * large number of series being extracted before processing. This method
	 * could be easily implemented in a future MapReduce application, where the
	 * extractedJSON would be a Mapper input and the output would be the
	 * SeriesResults. The Reducer would only group the SeriesResults of the same
	 * patient.
	 * 
	 * @return extraction status (TODO: Check if needed)
	 */
	private int extractAndProcess() {
		extractorProcessor = new JSONExtractor(this.inputPath);
		seriesProcessor = new SeriesProcessor();
		patientManager = new PatientManager();

		extractorProcessor.initialize();

		/***
		 * If true, the hasNext extracts another series. The user can have
		 * access to it by its getSeries() method.
		 */
		while (extractorProcessor.hasNext()) {

			currentSeries = extractorProcessor.getSeries();

			if (LOG.isDebugEnabled()) {
				LOG.debug("current number of extracted series:"
						+ extractorProcessor.getNumberOfExtractedSeries());
			}

			seriesProcessor.process(currentSeries);

			// Updates the Patient Manager list with that user information.
			patientManager.updateUserList(currentSeries,
					seriesProcessor.getSeriesResults());
		}

		patientManager.organizeList();

		verifier.verify(patientManager);

		generateOutput();
		return 0;
	}

	/***
	 * Prints all output associated information.
	 */
	private void generateOutput() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("==============================================");
			LOG.debug("generating output...");
		}
		System.out.println(patientManager.toString());

		ReferencesParameters.printParameters();
		
		System.out.println(verifier.toString());
		
		ValidationPrinter vPrinter = new ValidationPrinter(verifier, inputPath, outputPath);
		vPrinter.write();
	}
}

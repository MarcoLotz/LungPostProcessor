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

import com.marcolotz.lung.io.outputFormat.SeriesDataWritable;
import com.marcolotz.lung.posprocesser.data.SeriesResults;

/**
 * Receives a series and processes it. Finding nodules in the series.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class SeriesProcessor {

	/***
	 * The results of the processed series
	 */
	SeriesResults seriesResults;

	/***
	 * The series that will be processed.
	 */
	SeriesDataWritable seriesToProcess;

	public SeriesProcessor() {
	}

	/***
	 * Processes the series and validates the nodules found so far according to
	 * the reference paper (R1, R2, R3) criteria.
	 * 
	 * @param series
	 */
	public void process(SeriesDataWritable series) {
		this.seriesToProcess = series;

		seriesResults = new SeriesResults(seriesToProcess);

		/* Detect nodules only by correlation between images */
		seriesResults.process(seriesToProcess);

		/*
		 * Check if any of the detected nodules so far (by correlation) match
		 * the papers criteria.
		 */
		seriesResults.validateNodules();
	}

	/***
	 * @return seriesResults
	 */
	public SeriesResults getSeriesResults() {
		return this.seriesResults;
	}
}

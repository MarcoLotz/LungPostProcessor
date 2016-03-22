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


import com.marcolotz.lung.mapreduce.io.outputFormat.SeriesDataWritable;
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

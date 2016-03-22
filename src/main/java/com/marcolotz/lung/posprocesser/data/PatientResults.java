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
package com.marcolotz.lung.posprocesser.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.marcolotz.lung.mapreduce.io.outputFormat.SeriesDataWritable;


/**
 * Describes a patient from which at least a series was verified for nodules.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class PatientResults implements Comparable<PatientResults> {

	private static final Logger LOG = Logger.getLogger(PatientResults.class);

	/***
	 * DICOM patient information.
	 */
	private String patientsID;
	private String patientsName;

	/***
	 * By default, no one has nodules.
	 */
	boolean hasNodules = false;

	/***
	 * Contains all the results of the series related to that patient.
	 */
	ArrayList<SeriesResults> resultsList;

	public PatientResults(SeriesDataWritable series) {
		this.patientsID = series.getKeyStructureWritable().getPatientsID();
		this.patientsName = series.getKeyStructureWritable().getPatientsName();

		hasNodules = false;

		resultsList = new ArrayList<SeriesResults>();
	}

	/**
	 * @return the patientsID
	 */
	public String getPatientsID() {
		return patientsID;
	}

	/**
	 * @return the patientsName
	 */
	public String getPatientsName() {
		return patientsName;
	}

	/**
	 * @return the hasNodules
	 */
	public boolean hasNodules() {
		return hasNodules;
	}

	/**
	 * @return the resultsList
	 */
	public ArrayList<SeriesResults> getResultsList() {
		return resultsList;
	}

	/**
	 * @param resultsList
	 *            the resultsList to set
	 */
	public void setResultsList(ArrayList<SeriesResults> resultsList) {
		this.resultsList = resultsList;
	}

	/***
	 * Adds the seriesResults to the list and, in case of positive for nodules,
	 * updates the patient information.
	 * 
	 * @param seriesResults
	 */
	public void addResults(SeriesResults seriesResults) {

		if (seriesResults.hasNodules() == true) {
			this.hasNodules = true;

			if (LOG.isDebugEnabled()) {
				LOG.debug("User" + this.patientsID + "has nodules due to: "
						+ seriesResults.seriesInstanceUID);
			}
		}
		this.resultsList.add(seriesResults);
	}

	public String toString() {
		String buffer = new String("");

		buffer = buffer + "Patient ID: " + patientsID + "\n";
		buffer = buffer + "Patient Name: " + patientsName + "\n";
		buffer = buffer + "Number of associated series: " + resultsList.size()
				+ "\n";

		buffer = buffer + "Has Nodules? " + this.hasNodules + "\n";

		if (hasNodules) {
			Iterator<SeriesResults> itr = resultsList.iterator();

			while (itr.hasNext()) {
				SeriesResults series = itr.next();
				buffer = buffer + "\tSeries: " + series.seriesInstanceUID
						+ "\n";
				buffer = buffer + "\tNodes Dectected?: " + series.hasNodules()
						+ "\n";
			}
		}

		return buffer;
	}

	/***
	 * Compares two PatientsResults taking into consideration the userID number.
	 * Will sort it from the lower Patient ID to the higher patient ID (thus
	 * increasing ordering).
	 */
	@Override
	public int compareTo(PatientResults userThere) {
		/*
		 * Removes all spaces and non-visible characters, like tabs from the
		 * string
		 */
		String idHereStr = this.patientsID.replaceAll("\\s+", "");
		String idThereStr = userThere.patientsID.replaceAll("\\s+", "");

		int idHere = Integer.parseInt(idHereStr);
		int idThere = Integer.parseInt(idThereStr);

		if (idHere == idThere) {
			return 0;
		} else {
			if (idHere > idThere) {
				return +1;
			} else {
				return -1;
			}
		}
	}
}

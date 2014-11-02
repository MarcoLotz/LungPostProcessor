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
package com.marcolotz.lung.posprocesser.data;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.marcolotz.lung.io.outputFormat.SeriesDataWritable;

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

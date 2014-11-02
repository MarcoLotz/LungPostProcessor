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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.marcolotz.lung.io.outputFormat.SeriesDataWritable;
import com.marcolotz.lung.posprocesser.data.SeriesResults;
import com.marcolotz.lung.posprocesser.data.PatientResults;

/**
 * Handles all the Patients and their seriesResults
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class PatientManager {

	private static final Logger LOG = Logger.getLogger(PatientManager.class);

	/***
	 * List with all Patients that have been found so far.
	 */
	ArrayList<PatientResults> patientList;

	/***
	 * Number of patients detected with nodules so far.
	 */
	int nPatientsWithNodules = 0;

	public PatientManager() {
		patientList = new ArrayList<PatientResults>();
	}

	/**
	 * If the patient already exists in the list, adds the series to it.
	 * Otherwise creates a new user. It uses the PatientID attribute to verify
	 * if the patient already exists.
	 */
	public void updateUserList(SeriesDataWritable series,
			SeriesResults seriesResults) throws NullPointerException {
		Iterator<PatientResults> itr = patientList.iterator();
		PatientResults user = null;

		if (seriesResults == null) {
			throw new NullPointerException("No series result.");
		}

		boolean foundUser = false;

		while (itr.hasNext()) {
			user = itr.next();

			if (user.getPatientsID().equals(
					series.getKeyStructureWritable().getPatientsID())) {

				foundUser = true;
				break;
			}
		}

		/* If the user is not in the list yet: */
		if (!foundUser) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("New user added to list");
			}

			user = new PatientResults(series);
			patientList.add(user);
		}

		/* add results list and updates the user nodule information */
		user.addResults(seriesResults);
	}

	@Override
	public String toString() {
		/* Makes sure that it prints fresh statistics data */
		UpdateStatistics();

		String buffer = new String();
		Iterator<PatientResults> itr = patientList.iterator();

		while (itr.hasNext()) {
			PatientResults user = itr.next();

			buffer = buffer + "\n";
			buffer = buffer + user.toString();
		}

		buffer = buffer + "\n";
		buffer = buffer + "Total number of patients with nodules: "
				+ this.nPatientsWithNodules + "\n";
		buffer = buffer + "Total number of patients without nodules: "
				+ (patientList.size() - this.nPatientsWithNodules) + "\n";
		buffer = buffer + "Total number of Unique patients: "
				+ patientList.size() + "\n";

		return buffer;
	}

	/***
	 * Updates the Manager Statistics, as the number of patients with nodules.
	 */
	public void UpdateStatistics() {
		nPatientsWithNodules = 0;
		Iterator<PatientResults> itr = patientList.iterator();

		while (itr.hasNext()) {
			if (itr.next().hasNodules()) {
				nPatientsWithNodules++;
			}
		}
	}

	/***
	 * Sorts the Patient list by PatientID. It was used rather than by name due
	 * to the possibility of the name being hidden in future implementations.
	 */
	public void organizeList() {
		Collections.sort(patientList);
	}

	/**
	 * @return the userList
	 */
	public ArrayList<PatientResults> getUserList() {
		return patientList;
	}
}

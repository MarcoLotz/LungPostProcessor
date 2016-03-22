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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.marcolotz.lung.mapreduce.io.outputFormat.SeriesDataWritable;
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

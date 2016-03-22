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
package com.marcolotz.lung.posprocesser.debug;

import java.util.Iterator;

import com.marcolotz.lung.posprocesser.core.PatientManager;
import com.marcolotz.lung.posprocesser.data.PatientResults;

/**
 * This is just a class used just for extracting results. Probably a unit test/
 * -ea on java would be a more elegant solution.
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class ResultVerifier {

	int truePositives = 0;
	int falsePositives = 0;
	int trueNegatives = 0;
	int falseNegatives = 0;

	double normalizedTruePositives = 0;
	double normalizedFalsePositives = 0;

	int totalNumberOfResults = 0;

	/***
	 * Its the distance of a given result to the optimum that is 1
	 * normalizedTruePositives and 0 normalizedFalsePositives.
	 */
	float distanceToOptimum = 2;

	public void verify(PatientManager manager) {
		boolean expectedResult = false;

		Iterator<PatientResults> itr = manager.getUserList().iterator();

		while (itr.hasNext()) {
			PatientResults user = itr.next();

			expectedResult = getExpectedResult(user);
			compareResults(expectedResult, user);
		}

		totalNumberOfResults = truePositives + falsePositives + trueNegatives
				+ falseNegatives;

		normalizedTruePositives = ((float) truePositives)
				/ ((float) truePositives + (float) falseNegatives);
		normalizedFalsePositives = ((float) falsePositives)
				/ ((float) falsePositives + (float) trueNegatives);

		float distanceTruePositives = (float) (1 - normalizedTruePositives);
		float distanceFalsePositives = (float) (0 - normalizedFalsePositives);
		distanceToOptimum = (float) Math.sqrt((float) Math.pow(
				distanceTruePositives, 2)
				+ (float) Math.pow(distanceFalsePositives, 2));

	}

	/***
	 * Gets the expected result from the patient ID.
	 * 
	 * @param user
	 * @return true if the result of the patient matches the expected result
	 */
	private boolean getExpectedResult(PatientResults user) {

		/* Removes any space in the user ID string */
		String userID = user.getPatientsID().replaceAll("\\s+", "");
		switch (userID) {
		case ("0002505672"):
		case ("0003840217"):
		case ("0005579745"):
		case ("0006616102"):
		case ("0008328381"):
		case ("0008706636"):
		case ("0002593831"):
		case ("0004345383"):
		case ("0005658678"):
		case ("0007208247"):
		case ("0008330766"):
		case ("0008723808"):
		case ("0002923008"):
		case ("0004514382"):
		case ("0005737854"):
		case ("0007259787"):
		case ("0008332152"):
		case ("0008757974"):
		case ("0003054094"):
		case ("0004796225"):
		case ("0005762059"):
		case ("0007466089"):
		case ("0008485383"):
		case ("0003204346"):
		case ("0004939572"):
		case ("0006025503"):
		case ("0007997058"):
		case ("0008611457"):
		case ("0003555200"):
		case ("0005054325"):
		case ("0006379095"):
		case ("0008027509"):
		case ("0008637158"):
		case ("0003720756"):
		case ("0005214527"):
		case ("0006501111"):
		case ("0008160498"):
		case ("0008664927"):
		case ("0003733525"):
		case ("0005546126"):
		case ("0006589470"):
		case ("0008192659"):
		case ("0008700014"): {
			return true;
		}
		case ("0004762577"):
		case ("0006433875"):
		case ("0007010109"):
		case ("0007432622"):
		case ("0008553720"):
		case ("0005501451"):
		case ("0006608864"):
		case ("0007175284"):
		case ("0008523525"):
		case ("0008557627"): {
			return false;
		}
		default: {
			System.out.println("Error. A user ID was not expected:");
			System.out.println("UserID: " + user.getPatientsID());
			return false;
		}
		}
	}

	/***
	 * compares the expected result with the current result
	 * 
	 * @param expectedResult
	 * @param user
	 */
	private void compareResults(boolean expectedResult, PatientResults user) {
		boolean detectedResult = user.hasNodules();

		if ((expectedResult == true) && (detectedResult == true)) {
			truePositives++;
		} else if ((expectedResult == true) && (detectedResult == false)) {
			falseNegatives++;
		} else if ((expectedResult == false) && (detectedResult == true)) {
			falsePositives++;
		} else {
			trueNegatives++;
		}
	}

	@Override
	public String toString() {
		String buffer = new String();
		buffer = buffer + "Result verifier statistics:\n\n";

		buffer = buffer + "True positives: " + truePositives + "\n";
		buffer = buffer + "False positives: " + falsePositives + "\n";
		buffer = buffer + "True negatives: " + trueNegatives + "\n";
		buffer = buffer + "False negatives: " + falseNegatives + "\n";

		buffer = buffer + "Total number of verifications: "
				+ totalNumberOfResults + "\n";

		return buffer;
	}

	public String toFile() {
		return new String(normalizedTruePositives + "	"
				+ normalizedFalsePositives + "	" + distanceToOptimum);
	}

	/**
	 * @return the distanceOptimum
	 */
	public float getDistanceToOptimum() {
		return distanceToOptimum;
	}
}

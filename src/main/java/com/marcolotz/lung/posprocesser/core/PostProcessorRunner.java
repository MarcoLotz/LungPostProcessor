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

import org.apache.hadoop.conf.Configuration;

import com.marcolotz.lung.posprocesser.data.ReferencesParameters;

/**
 * A Launcher for the processor
 * 
 * @author Marco Aurelio Lotz
 * 
 */
public class PostProcessorRunner {
	
	/* Block executed when the class is created */
	static {
		/*
		 * The lungConfiguration.xml contains all the user defined
		 * configurations desired for a Hadoop job Don't forget to include it on
		 * the class path
		 */
		Configuration.addDefaultResource("./postConfiguration.xml");
		ReferencesParameters.conf = new Configuration();
	}

	static LungPostProcesser processer;

	public static void main(String[] args) {

		int exitCode = run(args);
		System.exit(exitCode);
	}

	public static int run(String[] args) {
		processer = new LungPostProcesser();

		processer.run(args);

		return 0;
	}
}

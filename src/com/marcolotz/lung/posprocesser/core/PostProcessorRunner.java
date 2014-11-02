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

/**
 * File: RewriteFile.java
 * Created by: mhaimel
 * Created on: Jul 26, 2011
 * CVS:  $Id: RewriteFile.java 1.0 Jul 26, 2011 3:22:56 PM mhaimel Exp $
 */
package samtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mhaimel
 *
 */
public class RewriteFile {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("/scratch/test/box/Tabix-test/input1.txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("/scratch/test/box/Tabix-test/input1r.txt"));
		String s = null;
		while((s = in.readLine()) != null){
			out.write(s+"\r");
		}
		out.close();
		in.close();
	}
}

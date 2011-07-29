/**
 * File: TabixCorrectnessTest.java
 * Created by: mhaimel
 * Created on: Jul 26, 2011
 * CVS:  $Id: TabixCorrectnessTest.java 1.0 Jul 26, 2011 3:50:23 PM mhaimel Exp $
 */
package samtools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import net.sf.samtools.util.BlockCompressedInputStream;
import net.sf.samtools.util.SeekableMemoryStream;

/**
 * @author mhaimel
 *
 */
public class TabixCorrectnessTest extends TabixSpeedTest {

	private File target;

	public TabixCorrectnessTest(TabixProvider tbp, File inputArr, File target) {
		super(tbp, inputArr);
		this.target = target;
	}
	
	@Override
	public void run() throws IOException {
		for(File in : input){
			System.out.println("Full test " + in + " for Count and Time...");
			byte[] bf = load(in);
			byte[] bfi = load(new File(in.getAbsolutePath()+".tbi"));
			char[] bt = loadReplaceLineSeparator(target);
			for(TabixReader tr : tbp.buildAll(bf,bfi)){
				char[] res = produceTabixOutput(tr);
				System.out.println(".."+tr.getClass().getName() + ":\t"+Arrays.equals(bt, res));  
			}
		}
	}

	private char[] loadReplaceLineSeparator(File file) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(bos));
		BufferedReader in = new BufferedReader(new FileReader(file));
		String s = null;
		while((s = in.readLine()) != null)
			out.println(s);
		in.close();
		out.close();
		return bos.toString().toCharArray();
	}

	private char[] produceTabixOutput(TabixReader reader) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(bos));
		String s = null;
		while((s = reader.readLine()) != null)
			out.println(s);
		out.close();
		return bos.toString().toCharArray();
	}

	public static void main(String[] args) throws IOException {
		File bFile = new File(args[0]);
		File tFile = new File(args[1]);
		TabixCorrectnessTest test = new TabixCorrectnessTest(new TabixProvider() {
			
			@Override
			public List<TabixReader> buildAll(byte[] bf, byte[] bfi) throws IOException {
				return Arrays.asList(new TabixReader[]{
						new TabixReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixFredReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixByteReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi)))
				}
				);
			}

		}, bFile,tFile);
		test.run();
	}
}

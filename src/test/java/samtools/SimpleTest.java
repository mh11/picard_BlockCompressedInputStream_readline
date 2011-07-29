/**
 * File: SimpleTest.java
 * Created by: mhaimel
 * Created on: Jul 25, 2011
 * CVS:  $Id: SimpleTest.java 1.0 Jul 25, 2011 4:08:01 PM mhaimel Exp $
 */
package samtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.sf.samtools.util.BlockCompressedInputStream;
import net.sf.samtools.util.SeekableMemoryStream;
import samtools.TabixReader.Iterator;
import samtools.TabixSpeedTest.TabixProvider;

/**
 * @author mhaimel
 *
 */
public class SimpleTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File f = new File(
//				"/scratch/test/box/test.bgzip"
				"/scratch/test/box/Tabix-test/input1.gz"
				);
		File fi = new File(
				f.getAbsolutePath()+".tbi"
				);
		TabixSpeedTest test = new TabixSpeedTest(new TabixProvider() {
			
			@Override
			public List<TabixReader> buildAll(byte[] bf, byte[] bfi) throws IOException {
				return Arrays.asList(new TabixReader[]{
						new TabixReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixFredReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixFredReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
				}
				);
			}

		}, new File("/scratch/test/box/Tabix-test/input1r.gz"));
		test.run();
		if(true)
			return;
//		System.out.println(test.isValid());
		byte[] bf = load(f);
		byte[] bfi = load(fi);
		int[] access = generateAccess(500,3349405);
		TabixReader readMem = null;
		TabixMatthiasReader lineReaderMem = null;
		TabixFredReader fredReaderMem = null;

		System.out.println("Start...");
		long start = 0;
		long end = 0;

//		readMem = new TabixReader(
//				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
//				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
//		start = System.currentTimeMillis();
//		readTabix("Memory:",
//				readMem);
//		end = System.currentTimeMillis();
//		System.out.println(end-start);
//
//		lineReaderMem = new TabixMatthiasReader(
//				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
//				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
//		start = System.currentTimeMillis();
//		readTabix(
//				"MemoryLn:",
//				lineReaderMem);
//		end = System.currentTimeMillis();
//		System.out.println(end-start);
//		
		fredReaderMem = new TabixFredReader(
				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
		start = System.currentTimeMillis();
		readTabix(
				"MemoryFred:",
				fredReaderMem);
		end = System.currentTimeMillis();
		System.out.println(end-start);
		
		readMem = 
			new TabixReader(
				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
		start = System.currentTimeMillis();
		readTabixRand("Random:",access,readMem);
		end = System.currentTimeMillis();
		System.out.println(end-start);

		lineReaderMem = 
			new TabixMatthiasReader(
				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
		start = System.currentTimeMillis();
		readTabixRand("RandomLn:",access,lineReaderMem);
		end = System.currentTimeMillis();
		System.out.println(end-start);
		

		fredReaderMem = 
			new TabixFredReader(
				new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
				new BlockCompressedInputStream(new SeekableMemoryStream(bfi)));
		start = System.currentTimeMillis();
		readTabixRand("RandomF:",access,fredReaderMem);
		end = System.currentTimeMillis();
		System.out.println(end-start);
	}

	private static void readTabixRand(String comment, int[] access,TabixReader reader) throws IOException {
		int count = 0;
		for(int idx : access){
			Iterator iter = reader.query("0:"+idx+"-"+idx);
			while (iter.next() != null)
				++count;
		}
		System.out.println(comment+"\t"+count);
	}

	private static int[] generateAccess(int size, int max) {
		int[] arr = new int[size];
		Random rand = new Random();
		for(int i = 0; i < size; ++i){
			Double d = rand.nextDouble()*(double)max;
			arr[i] = d.intValue();
		}
		return arr;
	}

	private static void readTabix(String comment, TabixReader reader) throws IOException {
		int count = 0;
		while (reader.readLine() != null)
			++count;
		System.out.println(comment+"\t"+count);
	}
	
	private static void readTabix2File(File out,String comment, TabixReader reader) throws IOException {
		PrintWriter o = new PrintWriter(new FileWriter(out,false));
		int count = 0;
		String s = null;
		while ((s = reader.readLine()) != null){
			++count;
			o.println(s);
		}
		o.close();
		System.out.println(comment+"\t"+count);
	}

	private static byte[] load(File f) throws IOException {
		int len = (int) f.length();
		byte[] arr = new byte[len];
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			int off = 0;
			while(off < len){
				int read = in.read(arr,off,len-off);
				off += read;
			}
			in.close();
		} finally{
			
		}
		return arr;
	}
}

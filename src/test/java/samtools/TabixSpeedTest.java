/**
 * File: TabixSpeedTest.java
 * Created by: mhaimel
 * Created on: Jul 25, 2011
 * CVS:  $Id: TabixSpeedTest.java 1.0 Jul 25, 2011 5:06:03 PM mhaimel Exp $
 */
package samtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import samtools.TabixReader.Iterator;

import net.sf.samtools.util.BlockCompressedInputStream;
import net.sf.samtools.util.SeekableMemoryStream;

/**
 * @author mhaimel
 *
 */
public class TabixSpeedTest {
	
	protected TabixProvider tbp;
	protected File[] input;

	public TabixSpeedTest(TabixProvider tbp,File ... inputArr) {
		this.tbp = tbp;
		this.input = inputArr;
	}
	
	public static interface TabixProvider{
		public List<TabixReader> buildAll(byte[] bf, byte[] bfi) throws IOException;
	}
	
	public void run() throws IOException{
		for(File in : input){
			System.out.println("Full test " + in + " for Count and Time...");
			byte[] bf = load(in);
			byte[] bfi = load(new File(in.getAbsolutePath()+".tbi"));
			for(TabixReader tr : tbp.buildAll(bf,bfi)){
				long[] res = produceTabix(tr);
				System.out.println(".."+tr.getClass().getName() + ":\t"+res[1]+"\t"+res[0]);  
			}
		}
	}
	public void runRand() throws IOException{
		for(File in : input){
			System.out.println("Random test " + in + " ...");
			byte[] bf = load(in);
			byte[] bfi = load(new File(in.getAbsolutePath()+".tbi"));
			String[] chrArr = null;
			for(TabixReader tr : tbp.buildAll(bf,bfi)){
				if(chrArr == null){
					chrArr = randChrArray(tr);
				}
				long[] res = produceTabixRand(tr, chrArr);
				System.out.println(".."+tr.getClass().getName() + ":\t"+res[1]+"\t"+res[0]); 
			}
		}
	}
	
	private long[] produceTabixRand(TabixReader reader, String[] chrArr) throws IOException {
		long count = 0;
		long time = System.currentTimeMillis();
		for(String s : chrArr){
			Iterator iter = reader.query(s);
			while(iter.next() != null)
				++count;
		}
		time = System.currentTimeMillis() - time;
		return new long[]{time,count};
	}
	private String[] randChrArray(TabixReader tr) {
		String[] chrArray = tr.getChrArray();
		ArrayList<String> list = new ArrayList<String>();
		for(String s : chrArray){
			if(!s.contains("_")){
				list.add(s);
			}
		}
		Collections.shuffle(list);
		return list.toArray(new String[0]);
	}

	private long[] produceTabix(TabixReader reader) throws IOException {
		long count = 0;
		long time = System.currentTimeMillis();
		while (reader.readLine() != null)
			++count;
		time = System.currentTimeMillis() - time;
		return new long[]{time,count};
	}
	

	protected BlockCompressedInputStream wrap(byte[] bf) {
		return new BlockCompressedInputStream(new SeekableMemoryStream(bf));
	}

	protected static byte[] load(File f) throws IOException {
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
	
	public static void main(String[] args) throws IOException {
		int s = Integer.valueOf(args[0]);
		File[] files = new File[args.length-1];
		for(int i = 1; i < args.length; ++i){
			files[i-1] = new File(args[i]);
		}
		TabixSpeedTest test = new TabixSpeedTest(new TabixProvider() {
			
			@Override
			public List<TabixReader> buildAll(byte[] bf, byte[] bfi) throws IOException {
				return Arrays.asList(new TabixReader[]{
						new TabixReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),

						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixByteReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixByteReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixByteReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixMatthiasReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
						new TabixByteReader(
								new BlockCompressedInputStream(new SeekableMemoryStream(bf)),
								new BlockCompressedInputStream(new SeekableMemoryStream(bfi))),
				}
				);
			}

		}, files);
		switch (s) {
		case 0:
			test.run();
			break;
		case 1:
			test.runRand();
			break;
		default:
			break;
		}
	}
}

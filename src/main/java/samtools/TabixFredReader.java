/**
 * File: TabixFredReader.java
 * Created by: mhaimel
 * Created on: Jul 26, 2011
 * CVS:  $Id: TabixFredReader.java 1.0 Jul 26, 2011 9:11:40 AM mhaimel Exp $
 */
package samtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.samtools.util.BlockCompressedInputStream;

/**
 * @author mhaimel
 *
 */
public class TabixFredReader extends TabixReader{

	private BufferedReader br;

	public TabixFredReader(BlockCompressedInputStream fn,
			BlockCompressedInputStream is) throws IOException {
		super(fn, is);
		br = new BufferedReader(new InputStreamReader(mFp));
	}
		
	@Override
	public String readLine() throws IOException {
		return br.readLine();
	}
	
	@Override
	public String readLine(BlockCompressedInputStream is) throws IOException {
		return br.readLine();
	}
	
	@Override
	protected void seek(long pos) throws IOException {
		super.seek(pos);
		br = new BufferedReader(new InputStreamReader(mFp));
	}
	
}

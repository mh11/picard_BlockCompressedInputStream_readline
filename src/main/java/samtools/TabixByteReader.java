/**
 * File: TabixMatthiasReader.java
 * Created by: mhaimel
 * Created on: Jul 25, 2011
 * CVS:  $Id: TabixMatthiasReader.java 1.0 Jul 25, 2011 3:50:41 PM mhaimel Exp $
 */
package samtools;

import java.io.IOException;

import net.sf.samtools.util.BlockCompressedInputStream;

/**
 * @author mhaimel
 *
 */
public class TabixByteReader extends TabixReader {

	public TabixByteReader(BlockCompressedInputStream fn,BlockCompressedInputStream is) throws IOException {
		super(fn, is);
	}
		
	public TabixByteReader(String fn) throws IOException {
		super(fn);
	}

	@Override
	public String readLine(BlockCompressedInputStream is) throws IOException {
		return is.readLine();
	}
}

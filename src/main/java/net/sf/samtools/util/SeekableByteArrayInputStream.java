/**
 * File: SeekableByteArrayInputStream.java
 * Created by: mhaimel
 * Created on: Jul 25, 2011
 * CVS:  $Id: SeekableByteArrayInputStream.java 1.0 Jul 25, 2011 3:59:29 PM mhaimel Exp $
 */
package net.sf.samtools.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author mhaimel
 * 
 */
public class SeekableByteArrayInputStream extends ByteArrayInputStream {

	public SeekableByteArrayInputStream(byte[] buf) {
		super(buf);
	}

	public void seek(long pos) throws IOException {
		this.pos = (int) pos;
	}

	public long length() {
		return (long) this.count;
	}
}

/**
 * File: SeekableMemoryStream.java
 * Created by: mhaimel
 * Created on: Jul 25, 2011
 * CVS:  $Id: SeekableMemoryStream.java 1.0 Jul 25, 2011 3:54:57 PM mhaimel Exp $
 */
package net.sf.samtools.util;

import java.io.IOException;

import net.sf.samtools.util.SeekableStream;


/**
 * @author mhaimel
 *
 */
public class SeekableMemoryStream extends  SeekableStream {
	
	private final SeekableByteArrayInputStream in;

	public SeekableMemoryStream(SeekableByteArrayInputStream in) {
		this.in = in;
	}
	
	public SeekableMemoryStream(byte[] in) {
		this(new SeekableByteArrayInputStream(in));
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public boolean eof() throws IOException {
		return in.available() <= 0;
	}

	@Override
	public String getSource() {
		return "Test in memory implementation";
	}

	@Override
	public long length() {
		return in.length();
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		return in.read(buffer, offset, length);
	}

	@Override
	public void seek(long position) throws IOException {
		in.seek(position);
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}
}

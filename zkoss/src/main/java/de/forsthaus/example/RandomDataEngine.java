/**
 * Copyright 2010 the original author or authors.
 * 
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package de.forsthaus.example;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zkoss.zk.ui.Sessions;

/**
 * Engine for creating random data from text files.<br>
 * 
 * 
 * @author bbruhns
 * @author sgerth
 */
public final class RandomDataEngine implements Serializable {

	private static final long serialVersionUID = 1L;

	final transient private static Random RANDOM;
	final transient private static String[] BLOB;
	final transient private static String[] EMAIL;
	final transient private static String[] HOMEPAGE;
	final transient private static String[] MANFIRSTNAME;
	final transient private static String[] LASTNAME;
	final transient private static String[] CITY;
	final transient private static String[] ZIP;
	final transient private static String[] STREET;
	final transient private static String[] PHONENUMBER;
	final transient private static String[] FEMALEFIRSTNAME;

	public static String getRandomBlob() {
		return BLOB[RANDOM.nextInt(BLOB.length)];
	}

	public static String getRandomEmail() {
		return EMAIL[RANDOM.nextInt(EMAIL.length)];
	}

	public static String getRandomHomepage() {
		return HOMEPAGE[RANDOM.nextInt(HOMEPAGE.length)];
	}

	public static String getRandomManFirstname() {
		return MANFIRSTNAME[RANDOM.nextInt(MANFIRSTNAME.length)];
	}

	public static String getRandomLastname() {
		return LASTNAME[RANDOM.nextInt(LASTNAME.length)];
	}

	public static String getRandomCity() {
		return CITY[RANDOM.nextInt(CITY.length)];
	}

	public static String getRandomZip() {
		return ZIP[RANDOM.nextInt(ZIP.length)];
	}

	public static String getRandomStreet() {
		return STREET[RANDOM.nextInt(STREET.length)];
	}

	public static String getRandomPhoneNumber() {
		return PHONENUMBER[RANDOM.nextInt(PHONENUMBER.length)];
	}

	public static String getRandomFemaleFirstname() {
		return FEMALEFIRSTNAME[RANDOM.nextInt(FEMALEFIRSTNAME.length)];
	}

	private static String[] createStringArray(String name) {
		try {
			return new MyReader(name).result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static {
		RANDOM = new Random((long) Math.random());
		BLOB = createStringArray("blob.txt");
		EMAIL = createStringArray("email.txt");
		HOMEPAGE = createStringArray("homepage.txt");
		MANFIRSTNAME = createStringArray("manfirstname.txt");
		LASTNAME = createStringArray("lastname.txt");
		CITY = createStringArray("city.txt");
		ZIP = createStringArray("zip.txt");
		STREET = createStringArray("street.txt");
		PHONENUMBER = createStringArray("phonenumber.txt");
		FEMALEFIRSTNAME = createStringArray("femalefirstname.txt");
	}

	final static class LineReader {
		public LineReader(InputStream inStream) {
			this.inStream = inStream;
		}

		byte[] inBuf = new byte[8192];

		char[] lineBuf = new char[1024];

		int inLimit = 0;

		int inOff = 0;

		InputStream inStream;

		int readLine() throws IOException {
			int len = 0;
			char c = 0;

			boolean skipWhiteSpace = true;
			boolean isNewLine = true;
			boolean appendedLineBegin = false;
			boolean precedingBackslash = false;
			boolean skipLF = false;

			while (true) {
				if (inOff >= inLimit) {
					inLimit = inStream.read(inBuf);
					inOff = 0;
					if (inLimit <= 0) {
						if (len == 0) {
							return -1;
						}
						return len;
					}
				}
				// The line below is equivalent to calling a
				// ISO8859-1 decoder.
				c = (char) (0xff & inBuf[inOff++]);
				if (skipLF) {
					skipLF = false;
					if (c == '\n') {
						continue;
					}
				}
				if (skipWhiteSpace) {
					if (c == ' ' || c == '\t' || c == '\f') {
						continue;
					}
					if (!appendedLineBegin && (c == '\r' || c == '\n')) {
						continue;
					}
					skipWhiteSpace = false;
					appendedLineBegin = false;
				}
				if (isNewLine) {
					isNewLine = false;
					if (c == '#' || c == '!') {
						continue;
					}
				}

				if (c != '\n' && c != '\r') {
					lineBuf[len++] = c;
					if (len == lineBuf.length) {
						int newLength = lineBuf.length * 2;
						if (newLength < 0) {
							newLength = Integer.MAX_VALUE;
						}
						char[] buf = new char[newLength];
						System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
						lineBuf = buf;
					}
					// flip the preceding backslash flag
					if (c == '\\') {
						precedingBackslash = !precedingBackslash;
					} else {
						precedingBackslash = false;
					}
				} else {
					// reached EOL
					if (len == 0) {
						isNewLine = true;
						skipWhiteSpace = true;
						len = 0;
						continue;
					}
					if (inOff >= inLimit) {
						inLimit = inStream.read(inBuf);
						inOff = 0;
						if (inLimit <= 0) {
							return len;
						}
					}
					if (precedingBackslash) {
						len -= 1;
						// skip the leading whitespace characters in following
						// line
						skipWhiteSpace = true;
						appendedLineBegin = true;
						precedingBackslash = false;
						if (c == '\r') {
							skipLF = true;
						}
					} else {
						return len;
					}
				}
			}
		}
	}

	private static class MyReader {
		final String[] result;

		MyReader(String name) throws IOException {
			super();
			String path = Sessions.getCurrent().getWebApp().getRealPath("/res") + "/";
			File file = new File(path + name);
			LineReader lr = new LineReader(new BufferedInputStream(new FileInputStream(file)));

			int limit;

			List<String> r = new ArrayList<String>();

			while ((limit = lr.readLine()) >= 0) {
				String value = new String(lr.lineBuf, 0, limit);
				r.add(value);
			}

			result = (String[]) r.toArray(new String[r.size()]);
		}
	}

	private RandomDataEngine() {
	}
}

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
package de.daibutsu.token;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author bbruhns
 * 
 */
final public class Md5Token {
	public final static int TOKEN_LENGTH = 6;

	final private MessageDigest md;

	final private String id;

	private long nextNewToken = 0;

	private long currentTokenTime = 0;

	private String currentToken = null;

	/**
	 * 
	 */
	public Md5Token(String id) {
		super();
		this.id = id;
		try {
			this.md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private long getCurrentHttpTokenTime() {
		setCurrentToken(null);
		try {
			final URL url = new URL("http://unixtime.forsthaus.de/time.php");
			final URLConnection conn = url.openConnection();
			final InputStream istream = conn.getInputStream();
			try {
				final StringBuilder sb = new StringBuilder();

				int ch = -1;
				while ((ch = istream.read()) != -1) {
					sb.append((char) ch);
				}
				long l1 = Long.parseLong(sb.toString());
				long ctt = l1 / 60;
				setCurrentTokenTime(ctt);
				setNextNewToken(System.currentTimeMillis() + ((ctt + 1) * 60 - l1) * 1000);

				return getCurrentTokenTime();
			} catch (NumberFormatException e) {
			} finally {
				istream.close();
			}
		} catch (IOException e) {
		}
		return System.currentTimeMillis() / 60000;
	}

	private String getCurrentToken() {
		return this.currentToken;
	}

	private long getCurrentTokenTime() {
		if (System.currentTimeMillis() > getNextNewToken()) {
			getCurrentHttpTokenTime();
		}

		return this.currentTokenTime;
	}

	private String getId() {
		return this.id;
	}

	private MessageDigest getMd() {
		return this.md;
	}

	private long getNextNewToken() {
		return this.nextNewToken;
	}

	public String getToken() {
		long tokenTime = getCurrentTokenTime();
		if (getCurrentToken() == null) {
			return getToken(tokenTime);
		}
		return getCurrentToken();
	}

	private String getToken(long tokenTime) {
		getMd().reset();

		byte[] bs = getMd().digest((getId() + String.valueOf(tokenTime) + getId()).getBytes());

		StringBuilder key = new StringBuilder();
		boolean t = tokenTime % 2 == 0;
		for (byte b : bs) {
			if (t = !t) {
				continue;
			}
			key.append(Math.abs(b % 10));
			if (key.length() >= TOKEN_LENGTH) {
				break;
			}
		}
		return key.substring(0, TOKEN_LENGTH);
	}

	public boolean isEqualsToken(String token) {
		long tokenTime = getCurrentTokenTime();

		if (isEqualsTokenImpl(token, tokenTime)) {
			return true;
		}
		--tokenTime;
		return isEqualsTokenImpl(token, tokenTime);
	}

	private boolean isEqualsTokenImpl(String token, long tokenTime) {
		return getToken(tokenTime).equals(token);
	}

	private void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}

	private void setCurrentTokenTime(long currentTokenTime) {
		this.currentTokenTime = currentTokenTime;
	}

	private void setNextNewToken(long nexNewToken) {
		this.nextNewToken = nexNewToken;
	}
}

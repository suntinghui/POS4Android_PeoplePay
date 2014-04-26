package com.people.util;

import android.text.Html;

public class StringUtil {

	/**
	 * Get a string where internal characters that are escaped for HTML are
	 * unescaped For example, '&amp;' becomes '&' Handles &#32; and &#x32; cases
	 * as well
	 * 
	 * @param str
	 * @return
	 */
	/***
	 * // ??????ï¼? public static String unescapingFromHTML(String str) { return
	 * Html.fromHtml(str).toString(); }
	 **/

	public static String unescapingFromHTML(String url) {
		if (null == url){
			return "";
		}
		
		return url.replace("&amp;", "&").replace("&lt;", "<")
				.replace("&gt;", ">").replace("&apos;", "\'")
				.replace("&quot;", "\"").replace("&nbsp;", " ")
				.replace("&copy;", "@").replace("&reg;", "?");

	}

	/**
	 * 
	 * å°?å­?ç¬?ä¸²ç???????? Unicode å½¢å?????å­?ç¬?ä¸?. å¦? "é»?" to "\u9EC4"
	 * 
	 * Converts unicodes to encoded \\uxxxx and escapes
	 * 
	 * special characters with a preceding slash
	 * 
	 * 
	 * 
	 * @param theString
	 * 
	 *            å¾?è½???¢æ??Unicodeç¼???????å­?ç¬?ä¸²ã??
	 * 
	 * @param escapeSpace
	 * 
	 *            ??????å¿½ç?¥ç©º??¼ï??ä¸?true??¶å?¨ç©º??¼å????¢æ????????ä¸?????????????
	 * 
	 * @return è¿????è½???¢å??Unicodeç¼???????å­?ç¬?ä¸²ã??
	 */

	public static String toEncodedUnicode(String theString, boolean escapeSpace) {
		if (null == theString || theString.trim().equals(""))
			return "";
		

		int len = theString.length();

		int bufLen = len * 2;

		if (bufLen < 0) {

			bufLen = Integer.MAX_VALUE;

		}

		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {

			char aChar = theString.charAt(x);

			// Handle common case first, selecting largest block that

			// avoids the specials below

			if ((aChar > 61) && (aChar < 127)) {

				if (aChar == '\\') {

					outBuffer.append('\\');

					outBuffer.append('\\');

					continue;

				}

				outBuffer.append(aChar);

				continue;

			}

			switch (aChar) {

			case ' ':

				if (x == 0 || escapeSpace)
					outBuffer.append('\\');

				outBuffer.append(' ');

				break;

			case '\t':

				outBuffer.append('\\');

				outBuffer.append('t');

				break;

			case '\n':

				outBuffer.append('\\');

				outBuffer.append('n');

				break;

			case '\r':

				outBuffer.append('\\');

				outBuffer.append('r');

				break;

			case '\f':

				outBuffer.append('\\');

				outBuffer.append('f');

				break;

			case '=': // Fall through

			case ':': // Fall through

			case '#': // Fall through

			case '!':

				outBuffer.append('\\');

				outBuffer.append(aChar);

				break;

			default:

				if ((aChar < 0x0020) || (aChar > 0x007e)) {

					// æ¯?ä¸?unicode???16ä½?ï¼?æ¯????ä½?å¯¹å?????16è¿???¶ä??é«?ä½?ä¿?å­???°ä??ä½?

					outBuffer.append('\\');

					outBuffer.append('u');

					outBuffer.append(toHex((aChar >> 12) & 0xF));

					outBuffer.append(toHex((aChar >> 8) & 0xF));

					outBuffer.append(toHex((aChar >> 4) & 0xF));

					outBuffer.append(toHex(aChar & 0xF));

				} else {

					outBuffer.append(aChar);

				}

			}

		}

		return outBuffer.toString();

	}

	/**
	 * 
	 * ä»? Unicode å½¢å?????å­?ç¬?ä¸²è½¬??¢æ??å¯¹å?????ç¼?????????¹æ??å­?ç¬?ä¸²ã?? å¦? "\u9EC4" to "é»?".
	 * 
	 * Converts encoded \\uxxxx to unicode chars
	 * 
	 * and changes special saved chars to their original forms
	 * 
	 * 
	 * 
	 * @param in
	 * 
	 *            Unicodeç¼???????å­?ç¬???°ç?????
	 * 
	 * @param off
	 * 
	 *            è½???¢ç??èµ·å?????ç§»é?????
	 * 
	 * @param len
	 * 
	 *            è½???¢ç??å­?ç¬???¿åº¦???
	 * 
	 * @param convtBuf
	 * 
	 *            è½???¢ç??ç¼?å­?å­?ç¬???°ç?????
	 * 
	 * @return å®????è½????ï¼?è¿????ç¼????????????¹æ??å­?ç¬?ä¸²ã??
	 */

	public static String fromEncodedUnicode(char[] in, int off, int len) {

		char aChar;

		char[] out = new char[len]; // ??????ä¸????

		int outLen = 0;

		int end = off + len;

		while (off < end) {

			aChar = in[off++];

			if (aChar == '\\') {

				aChar = in[off++];

				if (aChar == 'u') {

					// Read the xxxx

					int value = 0;

					for (int i = 0; i < 4; i++) {

						aChar = in[off++];

						switch (aChar) {

						case '0':

						case '1':

						case '2':

						case '3':

						case '4':

						case '5':

						case '6':

						case '7':

						case '8':

						case '9':

							value = (value << 4) + aChar - '0';

							break;

						case 'a':

						case 'b':

						case 'c':

						case 'd':

						case 'e':

						case 'f':

							value = (value << 4) + 10 + aChar - 'a';

							break;

						case 'A':

						case 'B':

						case 'C':

						case 'D':

						case 'E':

						case 'F':

							value = (value << 4) + 10 + aChar - 'A';

							break;

						default:

							throw new IllegalArgumentException(
									"Malformed \\uxxxx encoding.");

						}

					}

					out[outLen++] = (char) value;

				} else {

					if (aChar == 't') {

						aChar = '\t';

					} else if (aChar == 'r') {

						aChar = '\r';

					} else if (aChar == 'n') {

						aChar = '\n';

					} else if (aChar == 'f') {

						aChar = '\f';

					}

					out[outLen++] = aChar;

				}

			} else {

				out[outLen++] = (char) aChar;

			}

		}

		return new String(out, 0, outLen);

	}

	private static char toHex(int nibble) {

		return hexDigit[(nibble & 0xF)];

	}

	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A',

			'B', 'C', 'D', 'E', 'F' };

}

package com.isea.tools.args;

import sun.security.action.GetPropertyAction;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;

/**
 * Description: DecodeUtil
 * Author: liuzh
 * Update: liuzh(2014-03-18 12:07)
 */
public class DecodeUtil {
    private static String dfltEncName = null;
    private static final String DEFAULT_ENCODE = "ISO-8859-1";

    static {
        dfltEncName = AccessController.doPrivileged(
                new GetPropertyAction("file.encoding")
        );
    }

    public static String decode(String source, String encoding) throws UnsupportedEncodingException {
        int length = source.length();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if ((i + 2) < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    bos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                }
                else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            }
            else {
                bos.write(ch);
            }
        }
        return changed ? new String(bos.toByteArray(), encoding) : source;
    }

    public static String determineEncoding(HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = DEFAULT_ENCODE;
        }
        return enc;
    }

    public static String decodeInternal(HttpServletRequest request, String source) throws UnsupportedEncodingException {
        String enc = determineEncoding(request);
        try {
            return decode(source, enc);
        }
        catch (UnsupportedEncodingException ex) {
            return decode(source);
        }
    }

    public static String decode(String s) throws UnsupportedEncodingException{
        return decode2(s,dfltEncName);
    }

    public static String decode2(String s, String enc)
            throws UnsupportedEncodingException{

        boolean needToChange = false;
        int numChars = s.length();
        StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2 : numChars);
        int i = 0;

        if (enc.length() == 0) {
            throw new UnsupportedEncodingException ("URLDecoder: empty string enc args");
        }

        char c;
        byte[] bytes = null;
        while (i < numChars) {
            c = s.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;
                case '%':
                    try {

                        // (numChars-i)/3 is an upper bound for the number
                        // of remaining bytes
                        if (bytes == null)
                            bytes = new byte[(numChars-i)/3];
                        int pos = 0;

                        while ( ((i+2) < numChars) &&
                                (c=='%')) {
                            int v = Integer.parseInt(s.substring(i+1,i+3),16);
                            if (v < 0)
                                throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
                            bytes[pos++] = (byte) v;
                            i+= 3;
                            if (i < numChars)
                                c = s.charAt(i);
                        }

                        // A trailing, incomplete byte encoding such as
                        // "%x" will cause an exception to be thrown

                        if ((i < numChars) && (c=='%'))
                            throw new IllegalArgumentException(
                                    "URLDecoder: Incomplete trailing escape (%) pattern");

                        sb.append(new String(bytes, 0, pos, enc));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "URLDecoder: Illegal hex characters in escape (%) pattern - "
                                        + e.getMessage());
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }

        return (needToChange? sb.toString() : s);
    }
}

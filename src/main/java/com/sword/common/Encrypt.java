package com.sword.common;

import java.util.Base64;

import static com.sword.common.Util.concat;
import static com.sword.common.Util.split;

/**
 * Encrypt class
 *
 * @author max
 */
public class Encrypt {
    private final static Logger logger = Logger.newInstance(Encrypt.class);

    private static final byte[] BASE_64_MAP = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/', '='
    };

    private static final byte[] MIX_MAP = {
            'v', 'w', '0', '1', 'x', 'H', 'M', 'A', 'J', 's',
            'B', '9', '+', 'C', '4', 'a', 'i', 'N', 'O', 'j',
            '5', 'b', 'V', '7', '8', '/', '-', 'D', 'p', 'I',
            't', 'u', 'e', 'K', 'L', 'q', 'g', 'h', '6', 'W',
            'X', 'c', 'd', 'P', 'Q', 'R', 'y', 'z', 'F', 'G',
            'S', 'E', 'n', 'o', 'T', 'U', 'r', 'f', 'Y', 'Z',
            'k', 'l', 'm', '2', '3'
    };

    private static final int[][] chgTemple = {
            {4, 6, 5, 0, 2, 1, 3},
            {3, 2, 5, 1, 6, 4, 0},
            {5, 1, 3, 6, 4, 0, 2},
            {6, 2, 1, 0, 5, 3, 4},
            {1, 5, 4, 6, 2, 0, 3},
            {5, 2, 3, 1, 6, 4, 0}
    };

    public static String encode(String text, String key) {
        logger.debug("orgCode:" + text);
        String encoded = Base64.getEncoder().encodeToString(text.getBytes());
        logger.debug("base64Code:" + encoded);
        String mixedCode = mixcode(templeEncode(encoded), key);
        logger.debug("mixCode:" + mixedCode);
        return mixedCode;
    }

    public static String decode(String ciphertext, String key) {
//        logger.debug("mixCode:"+ciphertext);
        String fixedCode = templeDecode(fixcode(ciphertext, key));
//        logger.debug("fixedCode:"+fixedCode);
        String decoded = new String(Base64.getDecoder().decode(fixedCode));
//        logger.debug("decoded:"+decoded);
        return decoded;
    }

    private static String templeEncode(String srcStr) {
        int sumValue = getSumValue(srcStr);
        String[] splits = split(srcStr, chgTemple[0].length);
        int templeId = sumValue % chgTemple.length;

        String[] retArray = new String[splits.length];
        for (int p = 0; p < chgTemple[0].length; p++) {
            retArray[p] = splits[chgTemple[templeId][p]];
        }
        retArray[splits.length - 1] = splits[splits.length - 1];
        return concat(retArray);
    }

    private static String templeDecode(String srcStr) {
        int sumValue = getSumValue(srcStr);
        String[] splits = split(srcStr, chgTemple[0].length);
        int templeId = sumValue % chgTemple.length;

        String[] retArray = new String[splits.length];
        for (int p = 0; p < chgTemple[0].length; p++) {
            retArray[chgTemple[templeId][p]] = splits[p];
        }
        retArray[splits.length - 1] = splits[splits.length - 1];
        return concat(retArray);
    }

    private static String mixcode(String incode, String key) {
        int skipInterval = getSumValue(key);
        byte[] tmp = incode.getBytes();
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = MIX_MAP[(findCharIndex(tmp[i], BASE_64_MAP) + skipInterval) % 65];
        }
        return new String(tmp);
    }

    private static String fixcode(String mixedcode, String key) {
        int skipInterval = getSumValue(key);
        byte[] tmp = mixedcode.getBytes();
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = BASE_64_MAP[(findCharIndex(tmp[i], MIX_MAP) + 65 - skipInterval) % 65];
        }
        return new String(tmp);
    }

    private static int getSumValue(String key) {
        int sumValue = 0;
        byte[] k = key.getBytes();
        for (byte b : k) {
            sumValue += b;
            sumValue = sumValue % 64;
        }
//        logger.debug("Key["+key+"] sum value is "+ sumValue);
        return sumValue;
    }

    private static int findCharIndex(byte c, byte[] map) {
        int index = 0;

        for (int i = 0; i < map.length; i++) {
            if (map[i] == c) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static void main(String[] avgs) {
        String text = "{\"account\":\"olgeer\",\"password\":\"test\",\"action\":\"getCreditReport\"}";
        String key = "unuse";
        String encoded = Encrypt.encode(text, key);
        Logger.console("Encode: " + encoded);
        Logger.console("Decode: " + Encrypt.decode(encoded, key));

//        String tEncode = templeEncode(text);
//        Logger.console("exchange: " + tEncode);
//        String tDecode = templeDecode(tEncode);
//        Logger.console("rechange: " + tDecode);
    }
}
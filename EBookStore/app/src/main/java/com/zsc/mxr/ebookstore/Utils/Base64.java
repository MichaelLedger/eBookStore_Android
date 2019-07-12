package com.zsc.mxr.ebookstore.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/*切忌误用

可能会有人在不理解Base64编码的情况下，将其误用于数据加密或数据校验。

Base64是一种数据编码方式，目的是让数据符合传输协议的要求。标准Base64编码解码无需额外信息即完全可逆，即使你自己自定义字符集设计一种类Base64的编码方式用于数据加密，在多数场景下也较容易破解。

对于数据加密应该使用专门的目前还没有有效方式快速破解的加密算法。比如：对称加密算法AES-128-CBC，对称加密需要密钥，只要密钥没有泄露，通常难以破解；也可以使用非对称加密算法，如 RSA，利用极大整数因数分解的计算量极大这一特点，使得使用公钥加密的数据，只有使用私钥才能快速解密。

对于数据校验，也应该使用专门的消息认证码生成算法，如 HMAC - 一种使用单向散列函数构造消息认证码的方法，其过程是不可逆的、唯一确定的，并且使用密钥来生成认证码，其目的是防止数据在传输过程中被篡改或伪造。将原始数据与认证码一起传输，数据接收端将原始数据使用相同密钥和相同算法再次生成认证码，与原有认证码进行比对，校验数据的合法性。
*/

public class Base64 {
    static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static String encode(byte[] data) {
        return encode(data, 0, data.length, null).toString();
    }

    public static StringBuffer encode(byte[] data, int start, int len, StringBuffer buf) {
        if(buf == null) {
            buf = new StringBuffer(data.length * 3 / 2);
        }

        int end = len - 3;
        int i = start;
        int n = 0;

        int d;
        while(i <= end) {
            d = (data[i] & 255) << 16 | (data[i + 1] & 255) << 8 | data[i + 2] & 255;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append(charTab[d >> 6 & 63]);
            buf.append(charTab[d & 63]);
            i += 3;
            if(n++ >= 14) {
                n = 0;
                buf.append("\r\n");
            }
        }

        if(i == start + len - 2) {
            d = (data[i] & 255) << 16 | (data[i + 1] & 255) << 8;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append(charTab[d >> 6 & 63]);
            buf.append("=");
        } else if(i == start + len - 1) {
            d = (data[i] & 255) << 16;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append("==");
        }

        return buf;
    }

    static int decode(char c) {
        if(c >= 65 && c <= 90) {
            return c - 65;
        } else if(c >= 97 && c <= 122) {
            return c - 97 + 26;
        } else if(c >= 48 && c <= 57) {
            return c - 48 + 26 + 26;
        } else {
            switch(c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
        }
    }

    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            decode(s, bos);
        } catch (IOException var3) {
            throw new RuntimeException();
        }

        return bos.toByteArray();
    }

    public static void decode(String s, OutputStream os) throws IOException {
        int i = 0;
        int len = s.length();

        while(true) {
            while(i < len && s.charAt(i) <= 32) {
                ++i;
            }

            if(i == len) {
                break;
            }

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + decode(s.charAt(i + 3));
            os.write(tri >> 16 & 255);
            if(s.charAt(i + 2) == 61) {
                break;
            }

            os.write(tri >> 8 & 255);
            if(s.charAt(i + 3) == 61) {
                break;
            }

            os.write(tri & 255);
            i += 4;
        }

    }
}

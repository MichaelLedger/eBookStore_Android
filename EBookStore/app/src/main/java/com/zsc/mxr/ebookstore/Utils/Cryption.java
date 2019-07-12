package com.zsc.mxr.ebookstore.Utils;

import java.util.Random;

/*GTMBase64 from Google Toolbox*/
/*http://code.google.com/p/google-toolbox-for-mac/source/browse/trunk/Foundation/?r=87*/

public class Cryption {
    public static int PACKET_HEADER_SIZE = 5;

    /**
     * 加密算法
     *
     * @param strSndBuf
     *            需要加密的字符串
     * @param bEncryption
     *            true为加密，false为不加密
     * @return 加密后的字节数组
     */
    public static byte[] encryption(String strSndBuf, boolean bEncryption) {
        try {
            // byte[] pSndBuf =
            // System.Text.UTF8Encoding.UTF8.GetBytes(strSndBuf);
            byte[] pSndBuf = strSndBuf.getBytes("UTF-8");
            int iSize = pSndBuf.length;

            byte[] pTempBuffer = new byte[iSize + PACKET_HEADER_SIZE];

            if (bEncryption) {
                // rand find
                Random rd = new Random();
                int iRand = rd.nextInt();

                // pTempBuffer[0] = Convert.ToByte(iRand % 128);
                pTempBuffer[0] = (byte) (iRand % 128);
            } else {
                pTempBuffer[0] = 0;
            }

            // short iLength = Convert.ToInt16(iSize + PACKET_HEADER_SIZE);
            short iLength = (short) (iSize + PACKET_HEADER_SIZE);
            // byte[] byteLength = System.BitConverter.GetBytes(iLength);
            byte[] byteLength = toBytes(iLength);

            pTempBuffer[1] = byteLength[0];
            pTempBuffer[2] = byteLength[1];

            if (pTempBuffer[0] != 0) {
                for (int i = 0; i < iSize; i++) {
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // Convert.ToByte((Convert.ToInt32(pSndBuf[i]) + (i ^
                    // (Convert.ToInt32(pTempBuffer[0])))) % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // Convert.ToByte((Convert.ToInt32(pTempBuffer[PACKET_HEADER_SIZE
                    // + i]) ^ (Convert.ToInt32(pTempBuffer[0]) ^ (iSize - i)))
                    // % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // (byte)(((int)(pSndBuf[i]) + (i ^
                    // ((int)(pTempBuffer[0])))) % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // (byte)(((int)(pTempBuffer[PACKET_HEADER_SIZE + i]) ^
                    // ((int)(pTempBuffer[0]) ^ (iSize - i))) % 256);
                    pTempBuffer[PACKET_HEADER_SIZE + i] = (byte) (((pSndBuf[i] & 0xFF) + (i ^ ((pTempBuffer[0] & 0xFF)))) % 256);
                    pTempBuffer[PACKET_HEADER_SIZE + i] = (byte) (((pTempBuffer[PACKET_HEADER_SIZE + i] & 0xFF) ^ ((pTempBuffer[0] & 0xFF) ^ (iSize - i))) % 256);

                }
            } else {
                for (int i = 0; i < iSize; i++) {
                    pTempBuffer[PACKET_HEADER_SIZE + i] = pSndBuf[i];
                }
            }
            return pTempBuffer;
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * 加密算法
     *
     * @param strSndBuf
     *            需要加密的字符串
     * @param bEncryption
     *            true为加密，false为不加密
     * @return 加密后的字节数组
     */
    public static String encryptionToStr(String strSndBuf, boolean bEncryption) {
        try {
            // byte[] pSndBuf =
            // System.Text.UTF8Encoding.UTF8.GetBytes(strSndBuf);
            byte[] pSndBuf = strSndBuf.getBytes("UTF-8");
            int iSize = pSndBuf.length;

            byte[] pTempBuffer = new byte[iSize + PACKET_HEADER_SIZE];

            if (bEncryption) {
                // rand find
                Random rd = new Random();
                int iRand = rd.nextInt();

                // pTempBuffer[0] = Convert.ToByte(iRand % 128);
                pTempBuffer[0] = (byte) (iRand % 128);
            } else {
                pTempBuffer[0] = 0;
            }

            // short iLength = Convert.ToInt16(iSize + PACKET_HEADER_SIZE);
            short iLength = (short) (iSize + PACKET_HEADER_SIZE);
            // byte[] byteLength = System.BitConverter.GetBytes(iLength);
            byte[] byteLength = toBytes(iLength);

            pTempBuffer[1] = byteLength[0];
            pTempBuffer[2] = byteLength[1];

            if (pTempBuffer[0] != 0) {
                for (int i = 0; i < iSize; i++) {
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // Convert.ToByte((Convert.ToInt32(pSndBuf[i]) + (i ^
                    // (Convert.ToInt32(pTempBuffer[0])))) % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // Convert.ToByte((Convert.ToInt32(pTempBuffer[PACKET_HEADER_SIZE
                    // + i]) ^ (Convert.ToInt32(pTempBuffer[0]) ^ (iSize - i)))
                    // % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // (byte)(((int)(pSndBuf[i]) + (i ^
                    // ((int)(pTempBuffer[0])))) % 256);
                    // pTempBuffer[PACKET_HEADER_SIZE + i] =
                    // (byte)(((int)(pTempBuffer[PACKET_HEADER_SIZE + i]) ^
                    // ((int)(pTempBuffer[0]) ^ (iSize - i))) % 256);
                    pTempBuffer[PACKET_HEADER_SIZE + i] = (byte) (((pSndBuf[i] & 0xFF) + (i ^ ((pTempBuffer[0] & 0xFF)))) % 256);
                    pTempBuffer[PACKET_HEADER_SIZE + i] = (byte) (((pTempBuffer[PACKET_HEADER_SIZE + i] & 0xFF) ^ ((pTempBuffer[0] & 0xFF) ^ (iSize - i))) % 256);

                }
            } else {
                for (int i = 0; i < iSize; i++) {
                    pTempBuffer[PACKET_HEADER_SIZE + i] = pSndBuf[i];
                }
            }
            return android.util.Base64.encodeToString(pTempBuffer, android.util.Base64.NO_WRAP);
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * 解密算法
     *
     * @param content
     *            BASE64Encoder 处理后的字节数组
     * @return 返回解密后的字符串
     */
    public static String decryption(String content) {
        try {
            // BASE64Decoder dec = new BASE64Decoder();
            // byte[] pBuffer = dec.decodeBuffer(content);
            byte[] pBuffer = Base64.decode(content);

            String strBuffer = "";
            byte[] pNewBuffer = new byte[pBuffer.length - PACKET_HEADER_SIZE];
            int iSize = 0;

            iSize = pBuffer.length;

            if (pBuffer[0] != '\0') {
                for (int i = PACKET_HEADER_SIZE; i < iSize; i++) {
                    // int t1 = (Convert.ToInt32(pBuffer[i]) ^
                    // (Convert.ToInt32(pBuffer[0]) ^ (iSize - i))) % 256;
                    // int t1 = ((int)(pBuffer[i]) ^ ((int)(pBuffer[0]) ^ (iSize
                    // - i))) % 256;
                    int t1 = ((pBuffer[i] & 0xFF) ^ ((pBuffer[0] & 0xFF) ^ (iSize - i))) % 256;

                    // 取余数时 负数 加 256
                    if (t1 < 0) {
                        t1 = t1 + 256;
                    }

                    // pNewBuffer[i - PACKET_HEADER_SIZE] = Convert.ToByte(t1);
                    pNewBuffer[i - PACKET_HEADER_SIZE] = (byte) t1;
                    // int t2 = (Convert.ToInt32(pNewBuffer[i -
                    // PACKET_HEADER_SIZE]) - ((i - PACKET_HEADER_SIZE) ^
                    // Convert.ToInt32(pBuffer[0]))) % 256;
                    // int t2 = ((int)(pNewBuffer[i - PACKET_HEADER_SIZE]) - ((i
                    // - PACKET_HEADER_SIZE) ^ (int)(pBuffer[0]))) % 256;
                    int t2 = ((pNewBuffer[i - PACKET_HEADER_SIZE] & 0xFF) - ((i - PACKET_HEADER_SIZE) ^ (pBuffer[0] & 0xFF))) % 256;
                    // 取余数时 负数 加 256
                    if (t2 < 0) {
                        t2 = t2 + 256;
                    }

                    // pNewBuffer[i - PACKET_HEADER_SIZE] = Convert.ToByte(t2);
                    pNewBuffer[i - PACKET_HEADER_SIZE] = (byte) t2;
                }

                // 字符串 转换为UTF-8编码
                // strBuffer =
                // System.Text.UTF8Encoding.UTF8.GetString(pNewBuffer);
                strBuffer = new String(pNewBuffer, "UTF-8");
            }
            return strBuffer;
        } catch (Exception exp) {
            exp.printStackTrace();
            return "{}";
        }
    }

    static byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /* >> 0 */);

        return result;
    }

    static byte[] toBytes(short x) {

        // return new byte[]{(byte)((x>>8)&0xFF),(byte)(x&0xFF)};
        byte[] ret = new byte[2];
        ret[0] = (byte) (x & 0xff);
        ret[1] = (byte) ((x >> 8) & 0xff);
        return ret;

        // ByteBuffer buffer = ByteBuffer.allocate(2);
        // buffer.putShort(x);
        // buffer.flip();
        // return buffer.array();
    }
}

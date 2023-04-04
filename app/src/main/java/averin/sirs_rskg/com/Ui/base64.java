package averin.sirs_rskg.com.Ui;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class base64 {

    /**
     * BASE64解密
     *
     * @param key the String to be decrypted
     * @return byte[] the data which is decrypted
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key,Base64.DEFAULT);
    }
    /**
     * BASE64加密
     *
     * @param key the String to be encrypted
     * @return String the data which is encrypted
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeToString(key, Base64.DEFAULT);
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
//            String h = "";
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0; i<messageDigest.length; i++)
//                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//                h = Integer.toHexString(0xFF & messageDigest[i]);
//                while (h.length() < 2)
//                h = "0" + h;
//                hexString.append(h);

            StringBuilder hex = new StringBuilder(messageDigest.length * 2);

            for (byte b : messageDigest) {
                int i = (b & 0xFF);
                if (i < 0x10) hex.append('0');
                hex.append(Integer.toHexString(i));
            }

            return hex.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

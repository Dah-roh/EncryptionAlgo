package org.example;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;

public class MessageDecoder {

    public static String encodeMessage(String message) {
        String key = "89c4ec9f6b3f1086b158d8ef03dfe8155e6f79d9e66434b8f9b3432fe8720e50";
        String mac = generateMac(message, key);
        String encryptedMessage = encryptMessage(message, key);
        String packedMessage = packMessage(encryptedMessage, mac);
        return packedMessage;
    }

    private static String generateMac(String message, String key) {
        byte[] messageBytes = message.getBytes();
        byte[] keyBytes = HexUtils.hexStringToByteArray(key);

        try {
            // Create an HMAC-SHA-256 instance
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            hmacSha256.init(secretKey);
            byte[] hmac = hmacSha256.doFinal(messageBytes);
            return HexUtils.byteArrayToHexString(hmac);
        } catch (Exception ex) {
            return "";
        }
    }

    private static String encryptMessage(String message, String key) {
        try {
            // Generate a SecretKey from the provided key
            SecretKey secretKey = generateKey(key);

            // Create a Cipher instance for AES encryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the text
            byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));

            return HexUtils.byteArrayToHexString(encryptedBytes);
        } catch (Exception ex) {
            return "";
        }
    }

    private static String[] unpackMessage(String packedMessage) {
        int messageStart = packedMessage.indexOf("MS") + 4;
        int messageEnd = packedMessage.indexOf("MC");
        int macStart = packedMessage.indexOf("MC") + 4;

        String message = packedMessage.substring(messageStart, messageEnd);
        String mac = packedMessage.substring(macStart);

        return new String[]{message, mac};
    }

    private static String packMessage(String message, String mac) {
        String messageLength = StringUtils.leftPad(String.valueOf(message.length()), 2, '0');
        String macLength = StringUtils.leftPad(String.valueOf(mac.length()), 2, '0');
        String packedMessage = "MS" + messageLength + message + "MC" + macLength + mac;
        return packedMessage;
    }

    private static SecretKey generateKey(String key) throws Exception {
        byte[] keyBytes = HexUtils.hexStringToByteArray(key);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String decodeMessage(String packedMessage, String key) {
        // Extract the message and MAC from the packed message
        String message = unpackMessage(packedMessage)[0];
        String mac = unpackMessage(packedMessage)[1];
        // Verify the MAC
        if (!verifyMAC(decryptMessage(message, key), mac, key)) {
            return "Message authentication failed!";
        }

        // Decrypt the message
        String decryptedMessage = decryptMessage(message, key);

        return decryptedMessage;
    }
//
//    private static String extractMessage(String packedMessage) {
//        int messageStart = packedMessage.indexOf("MS") + 2;
//        int messageEnd = packedMessage.indexOf("MC");
//        return packedMessage.substring(messageStart, messageEnd);
//    }
//
//    private static String extractMAC(String packedMessage) {
//        int macStart = packedMessage.indexOf("MC") + 2;
//        return packedMessage.substring(macStart);
//    }

    private static boolean verifyMAC(String message, String mac, String key) {
        String calculatedMac = generateMac(message, key);
        return mac.equals(calculatedMac);
    }

    private static String decryptMessage(String encryptedMessage, String key) {
        try {
            SecretKey secretKey = generateKey(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] encryptedBytes = HexUtils.hexStringToByteArray(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, "UTF-8");
        } catch (Exception ex) {
            return "Decryption failed!";
        }
    }

    public static void main(String[] args) {
        String packedMessage = encodeMessage("Victor Oyekanmi pay me my money! LOL");
        String decryptionKey = "89c4ec9f6b3f1086b158d8ef03dfe8155e6f79d9e66434b8f9b3432fe8720e50";
        String decryptedMessage = decodeMessage(packedMessage, decryptionKey);
        System.out.println(decryptedMessage);
    }
}

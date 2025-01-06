package com.github.maximtereshchenko;

import com.diozero.devices.MFRC522;

import java.time.Instant;
import java.util.ArrayList;

final class Rfid {

    public static void main(String[] args) {
        try (MFRC522 mfrc522 = new MFRC522(0, 25)) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                if (mfrc522.requestA(new byte[2]) == MFRC522.StatusCode.OK) {
                    dump(mfrc522);
                    break;
                }
            }
        }
    }

    private static void dump(MFRC522 mfrc522) {
        var uid = mfrc522.readCardSerial();
        System.out.println("UID " + hex(uid.getUidBytes()));
        for (byte i = 0; i < 64; i++) {
            mfrc522.authenticate(true, i, MFRC522.DEFAULT_KEY, uid);
            System.out.printf("%d %s%n", i, hex(mfrc522.mifareRead(i)));
        }
        mfrc522.haltA();
        mfrc522.stopCrypto1();
    }

    private static String hex(byte[] bytes) {
        var hex = new ArrayList<String>();
        for (var data : bytes) {
            hex.add("%02x".formatted(data));
        }
        return hex.toString();
    }
}

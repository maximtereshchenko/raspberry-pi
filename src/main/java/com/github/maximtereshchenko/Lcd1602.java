package com.github.maximtereshchenko;

import com.diozero.api.I2CConstants;
import com.diozero.internal.provider.builtin.i2c.NativeI2CDeviceSMBus;
import com.diozero.sbc.DeviceFactoryHelper;

import java.util.concurrent.TimeUnit;

final class Lcd1602 implements AutoCloseable {

    private final NativeI2CDeviceSMBus bus = new NativeI2CDeviceSMBus(
        DeviceFactoryHelper.getNativeDeviceFactory(),
        "LCD1602",
        I2CConstants.CONTROLLER_1,
        0x27,
        I2CConstants.AddressSize.SIZE_7,
        false
    );

    private Lcd1602() {}

    static Lcd1602 newInstance() throws InterruptedException {
        var lcd = new Lcd1602();
        TimeUnit.MILLISECONDS.sleep(20);
        lcd.sendCommand((byte) 0b00110000); //Function set (interface 8-bit length)
        TimeUnit.MILLISECONDS.sleep(10);
        lcd.sendCommand((byte) 0b00110000); //Function set (interface 8-bit length)
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.sendCommand((byte) 0b00110000); //Function set (interface 8-bit length)
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.sendCommand((byte) 0b00100000); //Function set (interface 4-bit length)
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.sendCommand((byte) 0b00101000); //Function set (0010NFXX): N=1 - 2 lines, F=0 - 5x7 char size, XX=00 - font
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.sendCommand((byte) 0b00001000); //Display switch on/off (00001DCB): D=0 - display off, C=0 - cursor off, B=0 - blink off
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.clear();
        lcd.sendCommand((byte) 0b00000110); //Set moving direction of the cursor (000001MS): M=1 - increment mode, S=0 - no shift
        TimeUnit.MILLISECONDS.sleep(1);
        lcd.sendCommand((byte) 0b00001100); //Display switch on/off (00001DCB): D=0 - display on, C=0 - cursor off, B=0 - blink off
        TimeUnit.MILLISECONDS.sleep(1);
        return lcd;
    }

    @Override
    public void close() throws InterruptedException {
        clear();
    }

    void write(Position position, String line) throws InterruptedException {
        moveCursor(position);
        for (var i = 0; i < line.length(); i++) {
            sendData((byte) line.charAt(i));
        }
    }

    void clear() throws InterruptedException {
        sendCommand((byte) 0b00000001); //Clear screen
        TimeUnit.MILLISECONDS.sleep(2);
    }

    private void moveCursor(Position position) throws InterruptedException {
        switch (position) {
            case FIRST_LINE -> sendCommand((byte) 0b10000000); //Set display data RAM address counter (1L00XXXX): L=0 - first line, XXXX=0 - column 0
            case SECOND_LINE -> sendCommand((byte) 0b11000000);
        }
        TimeUnit.MILLISECONDS.sleep(1);
    }

    private void sendCommand(byte command) {
        var upperCommandBits = command & 0xF0;
        var lowerCommandBits = (command << 4) & 0xF0;
        var firstPacket = (byte) (upperCommandBits | 0b1100); //Command bits BL EN RW RS: RS=0 - command mode, RW=0 - write mode, EN=1 - first half of the command, BL=1 - enable background light
        var secondPacket = (byte) (upperCommandBits | 0b1000);
        var thirdPacket = (byte) (lowerCommandBits | 0b1100);
        var fourthPacket = (byte) (lowerCommandBits | 0b1000);
        bus.writeBytes(firstPacket, secondPacket, thirdPacket, fourthPacket);
    }

    private void sendData(byte data) throws InterruptedException {
        var upperCommandBits = data & 0xF0;
        var lowerCommandBits = (data << 4) & 0xF0;
        var firstPacket = (byte) (upperCommandBits | 0b1101); //Command bits BL EN RW RS: RS=1 - data mode, RW=0 - write mode, EN=1 - first half of the command, BL=1 - enable background light
        var secondPacket = (byte) (upperCommandBits | 0b1001);
        var thirdPacket = (byte) (lowerCommandBits | 0b1101);
        var fourthPacket = (byte) (lowerCommandBits | 0b1001);
        bus.writeBytes(firstPacket, secondPacket, thirdPacket, fourthPacket);
        TimeUnit.MILLISECONDS.sleep(1);
    }

    enum Position {
        FIRST_LINE, SECOND_LINE
    }
}

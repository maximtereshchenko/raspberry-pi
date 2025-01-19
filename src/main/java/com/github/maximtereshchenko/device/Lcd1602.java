package com.github.maximtereshchenko.device;

import com.diozero.api.I2CConstants;
import com.diozero.internal.provider.builtin.i2c.NativeI2CDeviceSMBus;
import com.diozero.sbc.DeviceFactoryHelper;

import java.util.concurrent.TimeUnit;

public final class Lcd1602 implements AutoCloseable {

    private final NativeI2CDeviceSMBus bus = new NativeI2CDeviceSMBus(
        DeviceFactoryHelper.getNativeDeviceFactory(),
        "LCD1602",
        I2CConstants.CONTROLLER_1,
        0x27,
        I2CConstants.AddressSize.SIZE_7,
        false
    );

    private Lcd1602() {}

    public static Lcd1602 newInstance() throws InterruptedException {
        var lcd = new Lcd1602();
        TimeUnit.MILLISECONDS.sleep(20);
        lcd.interfaceLength8Bit();
        lcd.interfaceLength8Bit();
        lcd.interfaceLength8Bit();
        lcd.interfaceLength4Bit();
        lcd.enable2Lines5width7heightCharacters();
        lcd.switchDisplay(false);
        lcd.clearScreen();
        lcd.configureCursor();
        lcd.switchDisplay(true);
        return lcd;
    }

    @Override
    public void close() throws InterruptedException {
        clearScreen();
        switchDisplay(false);
    }

    public void write(Position position, String data) throws InterruptedException {
        moveCursor(position);
        for (var i = 0; i < data.length(); i++) {
            writeData(data.charAt(i));
        }
    }

    public void clearScreen() throws InterruptedException {
        writeCommand(0b00000001);
    }

    private void interfaceLength8Bit() throws InterruptedException {
        writeCommand(0b00110000);
    }

    private void interfaceLength4Bit() throws InterruptedException {
        writeCommand(0b00100000);
    }

    private void enable2Lines5width7heightCharacters() throws InterruptedException {
        //Function set (0010NFXX): N=1 - 2 lines, F=0 - 5x7 char size, XX=00 - font
        writeCommand(0b00101000);
    }

    public void switchDisplay(boolean enable) throws InterruptedException {
        //Display switch on/off (00001DCB): D=0 - display off, C=0 - cursor off, B=0 - blink off
        var command = 0b00001000;
        if (enable) {
            command |= 0b100;
        }
        writeCommand(command, enable);
    }

    private void configureCursor() throws InterruptedException {
        //Set moving direction of the cursor (000001MS): M=1 - increment mode, S=0 - no shift
        writeCommand(0b00000110);
    }

    private void moveCursor(Position position) throws InterruptedException {
        writeCommand(
            switch (position) {
                //Set display data RAM address counter (1L00XXXX): L=0 - first line, XXXX=0 - column 0
                case FIRST_LINE -> 0b10000000;
                case SECOND_LINE -> 0b11000000;
            }
        );
    }

    private void writeCommand(int data) throws InterruptedException {
        writeCommand(data, true);
    }

    private void writeCommand(int data, boolean enableBacklight) throws InterruptedException {
        writeByte(PayloadType.COMMAND, data, enableBacklight, 15);
    }

    private void writeData(int data) throws InterruptedException {
        writeByte(PayloadType.DATA, data, true, 1);
    }

    private void writeByte(PayloadType type, int data, boolean enableBacklight, int sleepMilliseconds) throws InterruptedException {
        var upperBits = data & 0xF0;
        var lowerBits = (data << 4) & 0xF0;
        var firstPacketCommandBits = commandBits(type, Packet.FIRST, enableBacklight);
        var secondPacketCommandBits = commandBits(type, Packet.SECOND, enableBacklight);
        bus.writeBytes(
            (byte) (upperBits | firstPacketCommandBits),
            (byte) (upperBits | secondPacketCommandBits),
            (byte) (lowerBits | firstPacketCommandBits),
            (byte) (lowerBits | secondPacketCommandBits)
        );
        TimeUnit.MILLISECONDS.sleep(sleepMilliseconds);
    }

    private int commandBits(PayloadType type, Packet bits, boolean enableBacklight) {
        //Command bits BL EN RW RS: BL=1 - enable background light, EN=1 - first half of the command, RW=0 - write mode, RS=0 - command mode
        var commandBits = 0;
        if (type == PayloadType.DATA) {
            commandBits |= 1;
        }
        if (bits == Packet.FIRST) {
            commandBits |= 0b100;
        }
        if (enableBacklight) {
            commandBits |= 0b1000;
        }
        return commandBits;
    }

    public enum Position {
        FIRST_LINE, SECOND_LINE
    }

    private enum PayloadType {
        COMMAND, DATA
    }

    private enum Packet {
        FIRST, SECOND
    }
}

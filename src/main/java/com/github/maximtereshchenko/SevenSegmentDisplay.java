package com.github.maximtereshchenko;

import java.util.concurrent.TimeUnit;

final class SevenSegmentDisplay {

    public static void main(String[] args) throws InterruptedException {
        try (var chip = new Chip74HC595(27, 22, 17)) {
            display(
                chip,
                0b00000011, //0
                0b10011110, //1.
                0b00100101, //2
                0b00001100, //3.
                0b10011001, //4
                0b01001000, //5.
                0b11000001, //6
                0b00011110, //7.
                0b00000001, //8
                0b00011000 //9.
            );
        }
    }

    private static void display(Chip74HC595 chip, int... bitMasks) throws InterruptedException {
        for (var bitMask : bitMasks) {
            chip.shift(bitMask);
            chip.output();
            TimeUnit.SECONDS.sleep(1);
        }
    }
}

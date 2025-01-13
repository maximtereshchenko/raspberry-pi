package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Keypad;

import java.time.Instant;
import java.util.List;

final class MatrixKeypad {

    public static void main(String[] args) {
        try (var keypad = Keypad.from(List.of(10, 22, 27, 17), List.of(18, 23, 24, 25))) {
            var deadline = Instant.now().plusSeconds(15);
            while (Instant.now().isBefore(deadline)) {
                keypad.pressed().ifPresent(System.out::println);
            }
        }
    }
}

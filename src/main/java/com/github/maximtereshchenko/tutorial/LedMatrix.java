package com.github.maximtereshchenko.tutorial;

import com.github.maximtereshchenko.device.Chip74HC595;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

final class LedMatrix {

    public static void main(String[] args) throws InterruptedException {
        try (var chip = new Chip74HC595(27, 22, 17)) {
            animate(
                chip,
                """
                ***
                * *
                * *
                * *
                ***
                """,
                """
                 *
                **
                 *
                 *
                ***
                """,
                """
                ***
                  *
                ***
                *
                ***
                """,
                """
                ***
                  *
                ***
                  *
                ***
                """,
                """
                * *
                * *
                ***
                  *
                  *
                """,
                """
                ***
                *
                ***
                  *
                ***
                """,
                """
                ***
                *
                ***
                * *
                ***
                """,
                """
                ***
                  *
                  *
                  *
                  *
                """,
                """
                ***
                * *
                ***
                * *
                ***
                """,
                """
                ***
                * *
                ***
                  *
                ***
                """
            );
            display(
                Image.from(
                        """
                         *    *
                        ***  ***
                        * *  * *
                        ********
                        **    **
                        * *  * *
                        *      *
                        *  **  *
                        """
                    )
                    .frame(),
                chip,
                Duration.ofSeconds(10)
            );
        }
    }

    private static void animate(Chip74HC595 chip, String... images) throws InterruptedException {
        for (var image : images) {
            animate(Image.from(image), chip);
        }
    }

    private static void animate(Image image, Chip74HC595 chip) throws InterruptedException {
        for (var frame : frames(image)) {
            display(frame, chip, Duration.ofMillis(100));
        }
    }

    private static void display(Frame frame, Chip74HC595 chip, Duration duration) throws InterruptedException {
        var columns = frame.columns();
        var deadline = Instant.now().plus(duration.toNanos(), ChronoUnit.NANOS);
        while (Instant.now().isBefore(deadline)) {
            for (var i = 0; i < columns.length; i++) {
                chip.shift(columns[i]);
                chip.shift(0b11111111 ^ (1 << (columns.length - i - 1)));
                chip.output();
                TimeUnit.MILLISECONDS.sleep(3);
            }
        }
    }

    private static List<Frame> frames(Image image) {
        var frames = new ArrayList<Frame>();
        for (var i = 1; i < image.width(); i++) {
            frames.add(image.slice(i).frame());
        }
        frames.add(image.frame());
        var padded = image.padded();
        while (!frames.getLast().isEmpty()) {
            frames.add(padded.frame());
            padded = padded.padded();
        }
        return frames;
    }

    private static final class Image {

        private final String[] lines;

        private Image(String[] lines) {
            this.lines = lines.clone();
        }

        static Image from(String raw) {
            return new Image(raw.stripTrailing().split(System.lineSeparator()));
        }

        int width() {
            return Stream.of(lines)
                .mapToInt(String::length)
                .max()
                .orElse(0);
        }

        Image slice(int columns) {
            var sliced = new String[lines.length];
            for (var i = 0; i < lines.length; i++) {
                var to = width() - 1;
                sliced[i] = substring(lines[i], to - columns + 1, to);
            }
            return new Image(sliced);
        }

        Frame frame() {
            return Frame.from(lines);
        }

        Image padded() {
            var padded = new String[lines.length];
            for (var i = 0; i < lines.length; i++) {
                padded[i] = " " + lines[i];
            }
            return new Image(padded);
        }

        private String substring(String original, int from, int to) {
            if (original.isEmpty()) {
                return original;
            }
            if (from >= original.length()) {
                return "";
            }
            return original.substring(from, Math.min(to + 1, original.length()));
        }
    }

    private static final class Frame {

        private static final int SIZE = 8;
        private final String[] lines;

        private Frame(String[] lines) {
            this.lines = lines.clone();
        }

        static Frame from(String[] lines) {
            var margin = (SIZE - lines.length) / 2;
            var withMargin = new String[SIZE];
            for (var i = 0; i < withMargin.length; i++) {
                var index = i - margin;
                if (i < margin || index >= lines.length) {
                    withMargin[i] = "";
                } else {
                    withMargin[i] = lines[index];
                }
            }
            return new Frame(withMargin);
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            for (var lineIndex = 0; lineIndex < SIZE; lineIndex++) {
                if (lineIndex < lines.length) {
                    var line = lines[lineIndex];
                    for (var columnIndex = 0; columnIndex < SIZE; columnIndex++) {
                        if (columnIndex < line.length()) {
                            builder.append(line.charAt(columnIndex));
                        } else {
                            builder.append(" ");
                        }
                    }
                } else {
                    builder.append(" ".repeat(SIZE));
                }
                builder.append(System.lineSeparator());
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }

        int[] columns() {
            var columns = new int[SIZE];
            for (var lineIndex = 0; lineIndex < SIZE; lineIndex++) {
                if (lineIndex >= lines.length) {
                    continue;
                }
                var line = lines[lineIndex];
                for (var columnIndex = 0; columnIndex < SIZE; columnIndex++) {
                    if (columnIndex >= line.length()) {
                        break;
                    }
                    if (line.charAt(columnIndex) == '*') {
                        columns[columnIndex] = columns[columnIndex] ^ (1 << (SIZE - lineIndex - 1));
                    }
                }
            }
            return columns;
        }

        boolean isEmpty() {
            for (var lineIndex = 0; lineIndex < SIZE; lineIndex++) {
                if (lineIndex >= lines.length) {
                    return true;
                }
                var line = lines[lineIndex];
                for (var columnIndex = 0; columnIndex < SIZE; columnIndex++) {
                    if (columnIndex >= line.length()) {
                        break;
                    }
                    if (line.charAt(columnIndex) != ' ') {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}

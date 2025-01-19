package com.github.maximtereshchenko.airquality;

import com.diozero.api.DigitalInputDevice;

import java.time.Clock;
import java.time.ZoneId;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

final class Main {

    public static void main(String[] args) throws InterruptedException {
        var clock = Clock.system(zoneId(args));
        var latch = new CountDownLatch(1);
        try (
            var scheduler = Executors.newScheduledThreadPool(1);
            var button = new DigitalInputDevice(18);
            var display = new Lcd1602Display();
            var fineParticlesMatter25Indicator = new LedIndicator(26, 19, 13);
            var nitrogenDioxideIndicator = new LedIndicator(6, 5, 12);
            var carbonMonoxideIndicator = new LedIndicator(21, 20, 16)
        ) {
            Runtime.getRuntime().addShutdownHook(new Thread(onShutdown(latch, scheduler)));
            var turnOffFuture = new AtomicReference<ScheduledFuture<?>>();
            display.turnOff();
            var application = new Application(
                new OpenWeather(apiKey(args), cityName(args), clock),
                display,
                fineParticlesMatter25Indicator,
                nitrogenDioxideIndicator,
                carbonMonoxideIndicator
            );
            scheduler.scheduleAtFixedRate(
                () -> displayAirQuality(button, application, turnOffFuture, scheduler, delaySeconds(args)),
                0,
                500,
                TimeUnit.MILLISECONDS
            );
            latch.await();
        }
    }

    private static void displayAirQuality(
        DigitalInputDevice button,
        Application application,
        AtomicReference<ScheduledFuture<?>> turnOffFuture,
        ScheduledExecutorService scheduler,
        int delaySecond
    ) {
        if (!button.isActive()) {
            return;
        }
        application.displayAirQuality();
        var future = turnOffFuture.get();
        if (future != null) {
            future.cancel(false);
        }
        turnOffFuture.set(scheduler.schedule(application::turnOff, delaySecond, TimeUnit.SECONDS));
    }

    private static String apiKey(String[] args) {
        return args[0];
    }

    private static String cityName(String[] args) {
        return args[1];
    }

    private static ZoneId zoneId(String[] args) {
        return ZoneId.of(args[2]);
    }

    private static int delaySeconds(String[] args) {
        return Integer.parseInt(args[3]);
    }

    private static Runnable onShutdown(CountDownLatch latch, ScheduledExecutorService scheduler) {
        return () -> {
            try {
                latch.countDown();
                scheduler.shutdownNow();
                scheduler.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}

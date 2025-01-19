package com.github.maximtereshchenko.airquality;

import java.time.LocalTime;

record AirQuality(LocalTime time, double fineParticlesMatter25, double nitrogenDioxide, double carbonMonoxide) {}

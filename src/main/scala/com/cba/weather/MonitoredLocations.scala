package com.cba.weather

import java.time.LocalDateTime

import com.cba.weather.data.Location

object MonitoredLocations {
  val Locations = List(
    (Location(label = "Sydney", latitude = -33.920019, longitude = 150.924127), LocalDateTime.of(2019, 8, 1, 1, 0)),
    (Location(label = "Minsk", latitude = 53.893, longitude = 27.5674), LocalDateTime.of(2012, 1, 14, 1, 0)),
    (Location(label = "Cairo", latitude = 30.044, longitude = 31.340), LocalDateTime.of(2001, 12, 1, 1, 0)),
    (Location(label = "Vorkuta", latitude = 67.49884, longitude = 64.05253), LocalDateTime.of(2010, 5, 1, 1, 0)),
    (Location(label = "Rio De Janeiro", latitude = -22.970722, longitude = -43.182365), LocalDateTime.of(2010, 12, 1, 1, 0)),
    (Location(label = "Melbourne", latitude = -37.814, longitude = 144.96332), LocalDateTime.of(2019, 7, 21, 1, 0)),
    (Location(label = "Berlin", latitude = 52.520, longitude = 13.404), LocalDateTime.of(2010, 9, 1, 1, 0)),
    (Location(label = "Marrakesh", latitude = -31.669746, longitude = -7.973328), LocalDateTime.of(2010, 12, 6, 1, 0)),
    (Location(label = "Saint Petersburg", latitude = 59.93863, longitude = 30.31413), LocalDateTime.of(2019, 3, 1, 1, 0)),
    (Location(label = "Brisbane", latitude = -27.470125, longitude = 153.0210), LocalDateTime.of(2010, 8, 8, 1, 0)),
    (Location(label = "Tokyo", latitude = 35.652832, longitude = 139.8394), LocalDateTime.of(2012, 12, 18, 1, 0)),
    (Location(label = "Shenzhen", latitude = 22.54554, longitude = 114.0683), LocalDateTime.of(2011, 11, 13, 1, 0)),
    (Location(label = "Naples", latitude = 40.85631, longitude = 14.24641), LocalDateTime.of(2005, 1, 8, 1, 0)),
    (Location(label = "Caracas", latitude = 10.500, longitude = -66.9166), LocalDateTime.of(1983, 2, 20, 1, 0))
  )
}

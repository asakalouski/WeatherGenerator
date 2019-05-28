package com.cba.weather

import org.scalatest.FlatSpec
import org.scalatest._
import com.cba.weather.climate.ElevationCalculator._
import com.cba.weather.data.Location

class ElevationCalculatorTest extends FlatSpec with OptionValues with Matchers {

  "Elevation calculator" should "compute approximate elevation" in {
    val elevation = approximateElevation(Location(label = "Minsk", latitude = 53.893, longitude = 27.5674))
    assert(elevation.isDefined)
    elevation.value should equal(277)
  }

  it should "return None for out of range latitude and longitude" in {
    val elevation = approximateElevation(Location("Invalid", latitude = 200, longitude = 1000))
    assert(elevation.isEmpty)
    val elevationForOutOfRangeNegative = approximateElevation(Location("Invalid", latitude = -200, longitude = -300))
    assert(elevationForOutOfRangeNegative.isEmpty)
  }
}

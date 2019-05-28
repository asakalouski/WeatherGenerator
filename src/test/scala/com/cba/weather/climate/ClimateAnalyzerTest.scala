package com.cba.weather.climate

import java.time.LocalDateTime

import com.cba.weather.data.Location
import org.scalatest.FlatSpec
import com.cba.weather.climate.ClimateAnalyzer._
import com.cba.weather.data.Climate.{Polar, SubTropical, Temperate}
import com.cba.weather.data.Season.{Autumn, Spring, Summer, Winter}
import com.cba.weather.data.WeatherCondition.{Rain, Snow, Sunny}

class ClimateAnalyzerTest extends FlatSpec {

  implicit val generator = new UniformGenerator {
    override def nextInt(n: Int): Int = 1
  }

  implicit val normalGenerator = new NormalGenerator {
    override def nextGaussian(): Double = 0.5
  }

  "ClimateAnalyzer" should "identify season for location and time" in {
    val location1 = Location("Minsk", 67.49884, 64.05253)
    val time1 = LocalDateTime.of(2019, 6, 13, 0, 0)
    val season =  seasonForLocationAndTime(location1, time1)
    assert(season == Summer)
    val time2 = LocalDateTime.of(2019, 12, 13, 0, 0)
    val season2 = seasonForLocationAndTime(location1, time2)
    assert(season2 == Winter)

    val time3 = LocalDateTime.of(2019, 3, 13, 0, 0)
    val season3 = seasonForLocationAndTime(location1, time3)
    assert(season3 == Spring)

    //Cape Town
    val location2 = Location("Cape Town", -33.918861, 18.4233)
    val season4 = seasonForLocationAndTime(location2, time1)
    assert(season4 == Winter)

    val season5 = seasonForLocationAndTime(location2, time2)
    assert(season5 == Summer)

    val season6 = seasonForLocationAndTime(location2, time3)
    assert(season6 == Autumn)
  }

  it should "identify climate by location" in {
    val location1 = Location("Vorkuta", 67.49884, 64.05253)
    val climate1 = climateByLocation(location1).get
    assert(climate1 == Polar)

    val location2 = Location("Cape Town", -33.918861, 18.4233)
    val climate2 = climateByLocation(location2).get
    assert(climate2 == SubTropical)

    val location3 = Location("Minsk", 53.893, 27.5674)
    val climate3 = climateByLocation(location3).get
    assert(climate3 == Temperate)

    //invalid out of bounds location
    val location4 = Location("Invalid", 300, 300)
    val climate4 = climateByLocation(location4)
    assert(climate4.isEmpty)
  }

  it should "return temperature range for climate and season" in {
    val (low, high) = temperatureRangeForClimateAndSeason(Polar, Summer)
    assert(low == -1)
    assert(high == 15)
  }

  it should "return average rainy days/per month in season" in {
    val numberOfDays = averageRainyDays(SubTropical, Summer)
    assert(numberOfDays == 13)
  }

  it should "randomly increment time" in {
    var time = LocalDateTime.of(2015, 1, 1, 0, 0)
    time = randomTimeIncrement(time)
    val expectedDateTime = LocalDateTime.of(2015, 1, 2, 1, 1)
    assert(time.isEqual(expectedDateTime))
  }

  it should "generate random humidity for location and time" in {
    val time = LocalDateTime.of(2015, 1, 1, 0, 0)
    val location = Location("Minsk", 53.893, 27.5674)
    val h = humidity(location, time)
    assert(h.isDefined)
    assert(h.get == 93.0)
  }

  it should "generate random pressure for location" in {
    val location = Location("Cape Town", -33.918861, 18.4233)
    val p = pressure(location)
    assert(p.isDefined)
    assert(p.get == 1088.25)
  }

  it should "generate approximate weather condition for location and time" in {
    val time = LocalDateTime.of(2017, 12, 15, 0, 0)
    val location = Location("Cape Town", -33.918861, 18.4233)
    val c = approximateCondition(location, time)
    assert(c.isDefined)
    assert(c.get == Rain)

    val location2 = Location("Vorkuta", 67.49884, 64.05253)
    val c2 = approximateCondition(location2, time)
    assert(c2.isDefined)
    assert(c2.get == Snow)
  }

  it should "generate temperature with elevation adjust for location and time" in {
    val time = LocalDateTime.of(2017, 12, 15, 0, 0)
    val location = Location("Cape Town", -33.918861, 18.4233)
    val t = temperatureWithElevationAdjust(location, time)
    assert(t.isDefined)
    assert(t.get.intValue() == 28)
  }
}

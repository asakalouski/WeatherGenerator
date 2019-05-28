package com.cba.weather.climate

import java.time.LocalDateTime

import com.cba.weather.data.Climate._
import com.cba.weather.data.Season._
import com.cba.weather.data.{Climate, Location, Season, WeatherCondition}
import WeatherCondition._
import ElevationCalculator._

import scala.math.abs
import scala.math.exp
import scala.util.Random

object ClimateAnalyzer {

  private[this] val TropicCancer = 23.4367
  private[this] val SubtropicUpperLine = 40
  private[this] val TemperateUpperLine = 65
  private[this] val PolarUpperLine = 90

  private[this] val Seasons = List(Winter, Winter, Spring, Spring, Spring, Summer, Summer, Summer, Autumn, Autumn, Autumn, Winter)

  private[climate] def seasonForLocationAndTime(location: Location, time: LocalDateTime): Season =
    if (location.latitude >= 0) Seasons(time.getMonthValue - 1)
    else (Seasons ::: Seasons) (time.getMonthValue + 5)

  private[this] val TemperaturesAndRainyDays = Map[(Climate, Season), ((Double, Double), Int, Int)](
    (Tropical, Winter) -> ((10, 30), 9, 55), (Tropical, Spring) -> ((15, 36), 18, 61), (Tropical, Summer) -> ((20, 40), 18, 67), (Tropical, Autumn) -> ((14, 33), 18, 66),
    (SubTropical, Winter) -> ((2, 26), 8, 48), (SubTropical, Spring) -> ((4, 38), 11, 53), (SubTropical, Summer) -> ((9.5, 42), 13, 62), (SubTropical, Autumn) -> ((5, 32), 10, 58),
    (Temperate, Winter) -> ((-30, 10), 9, 88), (Temperate, Spring) -> ((-5, 20), 11, 67), (Temperate, Summer) -> ((8, 32), 19, 71), (Temperate, Autumn) -> ((-1, 18), 17, 88),
    (Polar, Winter) -> ((-48, -1), 25, 80), (Polar, Spring) -> ((-38, -12), 19, 79), (Polar, Summer) -> ((-1, 15), 20, 74), (Polar, Autumn) -> ((-29, 5), 15, 85)
  )

  private[climate] def climateByLocation(location: Location): Option[Climate] = location.latitude match {
    case t if (abs(t) <= TropicCancer) => Some(Tropical)
    case st if (abs(st) > TropicCancer && abs(st) <= SubtropicUpperLine) => Some(SubTropical)
    case tempr if (abs(tempr) > SubtropicUpperLine && abs(tempr) <= TemperateUpperLine) => Some(Temperate)
    case p if (abs(p) > TemperateUpperLine && abs(p) <= PolarUpperLine) => Some(Polar)
    case _ => None
  }

  private[climate] def temperatureRangeForClimateAndSeason(climate: Climate, season: Season): (Double, Double) = TemperaturesAndRainyDays((climate, season))._1

  private[climate] def averageRainyDays(climate: Climate, season: Season): Int = TemperaturesAndRainyDays((climate, season))._2

  private[climate] def averageRainyDays(location: Location, time: LocalDateTime): Option[Int] = climateByLocation(location)
    .map(climate => averageRainyDays(climate, seasonForLocationAndTime(location, time)))

  /**
    * Decrease in temperature per elevation. 9.8°C per 1,000 meters if weather condition is Sunny
    * if snow or rain then
    * 6°C per 1,000 meters
    *
    * @param location geographic location
    * @param time     time of the year
    * @return temperature decrease in degrees
    */
  private[climate] def elevationAdjust(location: Location, time: LocalDateTime)
                                      (implicit generator: UniformGenerator, normalGenerator: NormalGenerator): Option[Double] = for (
    elevation <- approximateElevation(location);
    condition <- approximateCondition(location, time)
  ) yield elevation.doubleValue() / 1000 * (if (condition == Sunny) -9.8 else -6)

  private[climate] def approximateTemperature(location: Location, time: LocalDateTime)
                                             (implicit normalGenerator: NormalGenerator): Option[Double] =
    climateByLocation(location)
      .map(climate => {
        val season = seasonForLocationAndTime(location, time)
        val (low, high) = temperatureRangeForClimateAndSeason(climate, season)
        val average = (high + low) / 2
        val sd = (high - average) / 3
        average + normalGenerator.nextGaussian() * sd
      })

  def temperatureWithElevationAdjust(location: Location, time: LocalDateTime)
                                    (implicit generator: UniformGenerator, normalGenerator: NormalGenerator): Option[Double] = for (
    elevationAdjust <- elevationAdjust(location, time);
    temperature <- approximateTemperature(location, time)
  ) yield (temperature - elevationAdjust)

  def approximateCondition(location: Location, time: LocalDateTime)
                          (implicit generator: UniformGenerator, normalGenerator: NormalGenerator): Option[WeatherCondition] = for (
    days <- averageRainyDays(location, time);
    temp <- approximateTemperature(location, time)
  ) yield (if (generator.nextInt(31) > days) Sunny else if (temp < 0) Snow else Rain)

  /**
    * Calculates atmospheric pressure using "Barometric Formula"
    *
    * @param location geo location
    * @return pressure in kPa
    */
  def pressure(location: Location)(implicit normalGenerator: NormalGenerator): Option[Double] =
    approximateElevation(location).map(h => (1013.25 + normalGenerator.nextGaussian() * 150) * exp(-0.00012 * h))

  /**
    *
    * @param location
    * @param time
    * @param generator
    * @param normalGenerator
    * @return
    */
  def humidity(location: Location, time: LocalDateTime)(implicit generator: UniformGenerator, normalGenerator: NormalGenerator): Option[Double] =
    for (
      condition <- approximateCondition(location, time);
      climate <- climateByLocation(location)
    ) yield condition match {
      case Rain => 100d
      case _ => TemperaturesAndRainyDays((climate, seasonForLocationAndTime(location, time)))._3 + 10 * normalGenerator.nextGaussian()
    }

  /**
    * Randomly increments days, hour and minutes of passed time object
    * @param time local date time object
    * @param generator random generator
    * @return
    */
  def randomTimeIncrement(time: LocalDateTime)(implicit generator: UniformGenerator): LocalDateTime =
    time.plusDays(generator.nextInt(5)).plusHours(generator.nextInt(12)).plusMinutes(generator.nextInt(60))

  implicit val normalGenerator = new NormalGenerator {
    override def nextGaussian(): Double = Random.nextGaussian()
  }

  implicit val generator = new UniformGenerator {
    override def nextInt(n: Int): Int = Random.nextInt(n)
  }
}

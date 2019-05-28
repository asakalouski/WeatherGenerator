package com.cba.weather.data

sealed trait WeatherCondition

object WeatherCondition {
  case object Rain extends WeatherCondition
  case object Snow extends WeatherCondition
  case object Sunny extends WeatherCondition
}


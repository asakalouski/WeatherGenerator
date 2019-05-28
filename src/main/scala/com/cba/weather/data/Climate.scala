package com.cba.weather.data

sealed trait Climate

object Climate {
  case object Polar extends Climate
  case object Temperate extends Climate
  case object SubTropical extends Climate
  case object Tropical extends Climate
}

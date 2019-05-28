package com.cba.weather.data

sealed trait Condition

object Condition {
  case object Rain extends Condition
  case object Snow extends Condition
  case object Sunny extends Condition
}


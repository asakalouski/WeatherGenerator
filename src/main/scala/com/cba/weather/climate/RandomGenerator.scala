package com.cba.weather.climate

trait RandomGenerator

trait UniformGenerator extends RandomGenerator {
  def nextInt(n:Int) : Int
}

trait NormalGenerator extends RandomGenerator {
  def nextGaussian() : Double
}

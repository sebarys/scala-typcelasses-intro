package com.sebarys.typelevel.functions

import org.scalatest.{FlatSpec, Matchers}

class P1FunctionsSpec extends FlatSpec with Matchers {

  behavior of "functions"

  it should "compose" in {

    //given
    val doubleValue: Int => Int =
      (value: Int) => value * 2
    val incrementValue: Int => Int =
      (value: Int) => value + 1

    //when
    val incrementAndDouble: Int => Int =
      incrementValue andThen doubleValue
    val doubleAndIncrement: Int => Int =
      incrementValue compose doubleValue

    //then
    incrementAndDouble(10) shouldEqual 22
    doubleAndIncrement(10) shouldEqual 21
  }

  it should "allow for creating higher-order functions" in {
    //higher-order functions - function that taking other function as parameter or returning function as a result

    //given
    // 1st order function - takes arguments and return plain value
    val double: Int => Int = (x: Int) => x * 2

    //when
    // map is higher-order function because takes a function and return 1st order function
    // to received function we can apply arguments to receive plain values
    val result = List(1, 2, 3, 4).map(double)

    //then
    result shouldEqual List(2, 4, 6, 8)


    //decorator pattern
    //given
    val testText = "some text"

    def htmlStringDecorator(text: String, decorate: String => String): String = {
      decorate(text)
    }

    def titleTag: String => String =
      (str: String) => s"<title>$str</title>"

    def boldTag: String => String =
      (str: String) => s"<b>$str</b>"

    //when
    val titleResult = htmlStringDecorator(testText, titleTag)
    val boldResult = htmlStringDecorator(testText, boldTag)

    //then
    titleResult shouldEqual s"<title>$testText</title>"
    boldResult shouldEqual s"<b>$testText</b>"
  }

  it should "allow for curring" in {
    //given

    //we can use currying to implement strategy pattern
    def calculateTax(taxStrategy: Double => Double)(annualSalary: Double): Double = {
      taxStrategy(annualSalary)
    }

    def twentyPercentageTaxCalculator: Double => Double = calculateTax(salary => salary * 0.2)

    def fortyPercentageTaxCalculator: Double => Double = calculateTax(salary => salary * 0.4)

    val income = 20000.0

    //when
    val twentyPercentageTax = twentyPercentageTaxCalculator(income)
    val fortyPercentageTax = fortyPercentageTaxCalculator(income)

    //then
    twentyPercentageTax shouldEqual 4000.0
    fortyPercentageTax shouldEqual 8000.0
  }

  it should "allow for creating polymorphic methods" in {
    //given
    def streamOfDuplicates[T](value: T): Stream[T] = Stream.continually(value)

    //when
    val streamOfInts: Stream[Int] = streamOfDuplicates(10)
    val streamOfDoubles: Stream[Double] = streamOfDuplicates(10.0)
    val streamOfStrings: Stream[String] = streamOfDuplicates("test")

    //then
    streamOfInts.take(5) foreach (int => int shouldEqual 10)
    streamOfDoubles.take(5) foreach (int => int shouldEqual 10.0)
    streamOfStrings.take(5) foreach (str => str shouldEqual "test")
  }

  it should "Exercise with plain function" in {

    //given
    // implement function that takes two functions and compose them
    def andThen(f1: Int => Double, f2: Double => String): Int => String = ???

    val doubleValue: Int => Double = (x: Int) => x * 2D
    val toResult: Double => String = (x: Double) => s"Result is $x"

    val doubleAndConvertToResult: Int => String = andThen(doubleValue, toResult)

    //given
    val result = doubleAndConvertToResult(10)

    //then
    result shouldEqual "Result is 20.0"

  }

  it should "Exercise with generic function" in {

    //given
    // implement function that takes two functions and compose them
    def andThen[A, B, C](f1: A => B, f2: B => C): A => C = ???

    val doubleValue: Int => Double = (x: Int) => x * 2D
    val toResult: Double => String = (x: Double) => s"Result is $x"

    val doubleAndConvertToResult: Int => String = andThen(doubleValue, toResult)

    //given
    val result = doubleAndConvertToResult(10)

    //then
    result shouldEqual "Result is 20.0"
  }
}

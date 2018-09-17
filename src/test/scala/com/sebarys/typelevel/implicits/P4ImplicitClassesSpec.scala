package com.sebarys.typelevel.implicits

import org.scalatest.{FlatSpec, Matchers}

class P4ImplicitClassesSpec extends FlatSpec with Matchers {

  behavior of "implicit classes"

  it should "allow for defining extension methods" in {

    //given
    import ExtensionMethods._

    //when
    val squareResult: Int = 4.square
    val stringToBooleanResult = "  false ".asBoolean

    //then
    squareResult shouldEqual (4 * 4)
    stringToBooleanResult shouldEqual false
  }

}

object ExtensionMethods {

  // implicit classes must be defined inside of another trait/class/object

  implicit class IntHelper(i: Int) {
    def square() = i * i
  }

  implicit class StringHelper(s: String) {
    def asBoolean = s.trim match {
      case "0" | "zero" | "" | " " | "false" => false
      case _ => true
    }
  }

}

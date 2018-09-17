package com.sebarys.typelevel.types

import org.scalatest.{FlatSpec, Matchers}

class P1TypesConstructorSpec extends FlatSpec with Matchers {
  behavior of "types"

  it should "allow to create proper types from type constructor (1st order kinded type)" in {

    //we have generic type Maybe and by providing concrete type to type constructor we receive concrete kind of Maybe
    //our generic type is just a context to which we provide our concrete type

    val justInt: Just[Int] = new Just(4)
    val emptyInt: Empty[Int] = new Empty[Int]()

    val justString: Just[String] = new Just("test")
    val emptyString: Empty[String] = new Empty[String]()

  }
}

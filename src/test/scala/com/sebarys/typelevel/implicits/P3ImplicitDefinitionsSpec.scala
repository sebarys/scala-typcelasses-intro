package com.sebarys.typelevel.implicits

import org.scalatest.{FlatSpec, Matchers}

class P3ImplicitDefinitionsSpec extends FlatSpec with Matchers {

  behavior of "implicit definitions"

  it should "allow for implicit conversions" in {

    //given

    //it is not recommended to do that thing so we need to add this import to force enable implicit conversions
    import scala.language.implicitConversions

    case class User(name: String)

    object User {
      implicit def int2User(x: Int): User =
        x match {
          case 1 => User("User1")
          case 2 => User("User2")
          case _ => User("unknown")
        }
    }

    //when
    val userFromInt: User = 1

    //then
    userFromInt shouldEqual User("User1")
  }

  it should "allow for using parameterized types in implicits e.g. for producing instances" in {

    //given
    implicit val str: String = "test"
    implicit val int: Int = 0

    implicit def streamOfDuplicates[A](implicit value: A): Stream[A] =
      Stream.continually(value)

    //when
    val streamOfStrings: Stream[String] = implicitly[Stream[String]]
    val streamOfInts: Stream[Int] = implicitly[Stream[Int]]

    //then
    streamOfStrings.take(5) foreach (str => str shouldEqual "test")
    streamOfInts.take(5) foreach (int => int shouldEqual 0)
  }
}

package com.sebarys.typelevel.types

import org.scalatest.{FlatSpec, Matchers}

class P3SemigroupSpec extends FlatSpec with Matchers {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  object Semigroup {
    def apply[A: Semigroup]: Semigroup[A] = implicitly[Semigroup[A]]

    implicit class SemigroupOps[A: Semigroup](x: A) {
      def combine(y: A): A = Semigroup[A].combine(x, y)
    }

  }

  implicit val intAdditionSemigroup = new Semigroup[Int] {
    override def combine(x: Int, y: Int): Int = x + y
  }

  behavior of "semigroup"

  it should "allow for combine ints when required instances are provided" in {

    //given
    import Semigroup._

    //when
    val res = 5.combine(6)

    //then
    res shouldEqual 11
  }

  /**
    * Exercise 1: Implement Semigroup for Map[A,B]
    */
  implicit def mapValueAdditionSemigroup[A, B: Semigroup] = new Semigroup[Map[A, B]] {
    import Semigroup._

    def optionCombine[B: Semigroup](x: B, maybeY: Option[B]): B =
      maybeY.map(x.combine(_)).getOrElse(x)

    override def combine(firstMap: Map[A, B], secondMap: Map[A, B]): Map[A, B] = {
      //we fold x map with y map as initial value
      firstMap.foldLeft(secondMap) {
        //for each key try to get value from init map and combine them
        case (acc, (firstMapKey, value)) => acc.updated(firstMapKey, optionCombine(value, acc.get(firstMapKey)))
      }
    }
  }

  it should "allow for combine map when values type provide instance of semigroup" in {

    //given
    val map1 = Map("a" -> 6, "b" -> 7, "c" -> 10)
    val map2 = Map("a" -> 4, "b" -> 3, "d" -> 10)
    import Semigroup._

    //when
    val res = map1 combine map2
    //then
    res shouldEqual Map("a" -> 10, "b" -> 10, "c" -> 10, "d" -> 10)
  }

  /**
    * Exercise 2: Implement Semigroup for our custom class - Order
    */
  case class Order(items: Map[String, Int])

  implicit val orderAdditionSemigroup = new Semigroup[Order] {
    override def combine(x: Order, y: Order): Order = ???
  }

  it should "allow for combine custom types and reuse existing combine implementations" in {

    //given

    import Semigroup._

    val order1 = Order(Map("item1" -> 6, "item2" -> 7, "item3" -> 10, "item5" -> 2))
    val order2 = Order(Map("item1" -> 4, "item3" -> 7))

    //when
    val res = order1 combine order2

    //then
    res shouldEqual Order(Map("item1" -> 10, "item2" -> 7, "item3" -> 17, "item5" -> 2))
  }

}

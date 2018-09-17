package com.sebarys.typelevel.types

import org.scalatest.{FlatSpec, Matchers}

class P5FunctorSpec extends FlatSpec with Matchers {

  /**
    * Functor allows us to abstract over type constructors (higher-kinded type) to provide ability to map over elements
    * - F[_] - means that we are specifying behaviour of context (F) not focus on element that is inside container/context
    */
  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object Functor {
    def apply[F[_] : Functor]: Functor[F] = implicitly[Functor[F]]

    implicit class FunctorOps[F[_] : Functor, A](src: F[A]) {
      def map[B](func: A => B): F[B] = Functor[F].map(src)(func)
    }

  }

  /**
    * Exercise implement Functor typeclass for following types
    */
  implicit val optionFunctor = new Functor[Option] {
    // we are focusing how Option (context) should behave
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
      case Some(a) => Some(f(a))
      case None => None
    }
  }

  implicit val maybeFunctor = new Functor[Maybe] {
    override def map[A, B](fa: Maybe[A])(f: A => B): Maybe[B] = fa match {
      case just: Just[A] => new Just(f(just.a))
      case _: Empty[A] => new Empty[B]
    }
  }

  case class Container[A](first: A, second: A)

  implicit val ourContainerFunctor = new Functor[Container] {
    override def map[A, B](originalContainer: Container[A])(f: A => B): Container[B] =
      Container(f(originalContainer.first), f(originalContainer.second))
  }

  trait Show[A] {
    def show(a: A): String
  }

  object Show {
    // if someone ask for Show[A] ask compiler to find implementation
    def apply[A: Show]: Show[A] = implicitly[Show[A]]

    // providing implicit class with extension methods for Show
    implicit class ShowOps[A: Show](a: A) {
      def show: String = Show[A].show(a)
    }

  }

  implicit val intShow = new Show[Int] {
    override def show(a: Int): String = a.toString
  }

  implicit val stringShow = new Show[String] {
    override def show(a: String): String = a
  }

  implicit def maybeShow[A: Show] = new Show[Maybe[A]] {

    import Show._

    override def show(a: Maybe[A]): String = a match {
      case just: Just[A] => just.a.show
      case _: Empty[A] => "Empty"
    }
  }

  behavior of "functor"

  it should "allow for map over values inside context/container" in {
    //given
    import Functor._
    import Show._

    val maybeInt: Maybe[Int] = new Just(5)
    val ourContainer = Container("firstVal", "secondVal")

    //when
    val maybeMappingResult: Maybe[String] = maybeInt.map(_.show)
    val ourContainerMappingResult: Container[Int] = ourContainer.map(_.length)

    //then
    maybeMappingResult.show shouldEqual "5"
    ourContainerMappingResult shouldEqual Container(8, 9)
  }
}

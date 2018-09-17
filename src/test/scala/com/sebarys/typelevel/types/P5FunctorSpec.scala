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
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = ???
  }

  implicit val maybeFunctor = new Functor[Maybe] {
    override def map[A, B](fa: Maybe[A])(f: A => B): Maybe[B] = ???
  }

  case class Container[A](first: A, second: A)

  implicit val ourContainerFunctor = new Functor[Container] {
    override def map[A, B](originalContainer: Container[A])(f: A => B): Container[B] = ???
  }

  behavior of "functor"

  it should "allow for map over values inside context/container" in {
    //given
    import Functor._

    val maybeInt: Maybe[Int] = new Just(5)
    val ourContainer = Container("firstVal", "secondVal")

    //when
    val maybeMappingResult: Maybe[String] = maybeInt.map(_.toString)
    val ourContainerMappingResult: Container[Int] = ourContainer.map(_.length)

  }
}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.sebarys.typelevel.types

import org.scalatest.{FlatSpec, Matchers}

class P2TypeclassesSpec extends FlatSpec with Matchers {

  class Invoice(val id: String, val company: String, val price: Double)

  //it is not spec at all, but it allows me to have more than one main in one place ;)
  behavior of "why we need typeclasses?"

  it should "let us create our super generic method that is doing our complicated business logic - creating string from object" in {

    //given
    def makeStringFrom[A](a: A): String = a.toString

    val someInt = 10
    val someDouble = 10.0
    val someInvoice = new Invoice("invoice-id", "our company", 500.0)

    //when
    val stringFromInt: String = makeStringFrom(someInt)
    val stringFromDouble: String = makeStringFrom(someDouble)
    val stringFromInvoice: String = makeStringFrom(someInvoice)

    //then
    stringFromInt shouldEqual "10"
    stringFromDouble shouldEqual "10.0"
    // oops, we are rely on the fact that all class in JVM has toString method
    // now we must trust to the caller of the method that it will pass a object with correctly implemented toString
    // we don't have any guarantees
    stringFromInvoice shouldEqual "Invoice(invoice-id, our company, 500.0)"
  }

  it should "maybe we can do it better" in {
    //given
    trait Show[A] {
      def show(a: A): String
    }

    def makeStringFrom[A](a: A)(show: Show[A]): String = show.show(a)

    val invoiceShow = new Show[Invoice] {
      override def show(a: Invoice): String = s"Invoice(${a.id}, ${a.company}, ${a.price})"
    }
    val someInvoice = new Invoice("invoice-id", "our company", 500.0)

    //when
    val stringFromInvoice = makeStringFrom(someInvoice)(invoiceShow)

    //then
    stringFromInvoice shouldEqual "Invoice(invoice-id, our company, 500.0)"
  }

  it should "we can use implicits to make our life easier" in {
    //given
    trait Show[A] {
      def show(a: A): String
    }

    def makeStringFrom[A](a: A)(implicit show: Show[A]): String = show.show(a)

    implicit val invoiceShow = new Show[Invoice] {
      override def show(a: Invoice): String = s"Invoice(${a.id}, ${a.company}, ${a.price})"
    }
    val someInvoice = new Invoice("invoice-id", "our company", 500.0)

    //when
    // compiler could find proper implementation of Show for us
    val stringFromInvoice = makeStringFrom(someInvoice)

    //then
    stringFromInvoice shouldEqual "Invoice(invoice-id, our company, 500.0)"
  }

  it should "maybe I can make constraint on type (it need to implement Show) and do not pass second argument to my function?" in {
    //given
    trait Show[A] {
      def show(a: A): String
    }
    object Show {
      // if someone ask for Show[A] compiler will try to find implementation
      def apply[A: Show]: Show[A] = implicitly[Show[A]]
    }

    def makeStringFrom[A: Show](a: A): String = Show[A].show(a)

    implicit val invoiceShow = new Show[Invoice] {
      override def show(a: Invoice): String = s"Invoice(${a.id}, ${a.company}, ${a.price})"
    }
    val someInvoice = new Invoice("invoice-id", "our company", 500.0)

    //when
    val stringFromInvoice = makeStringFrom(someInvoice)

    //then
    stringFromInvoice shouldEqual "Invoice(invoice-id, our company, 500.0)"
  }

  it should "I already know how to extend my type with extension methods, maybe it could be used here to receive more OO-syntax" in {
    //given
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

    import Show._

    implicit val invoiceShow = new Show[Invoice] {
      override def show(a: Invoice): String = s"Invoice(${a.id}, ${a.company}, ${a.price})"
    }
    val someInvoice = new Invoice("invoice-id", "our company", 500.0)

    //then
    someInvoice.show shouldEqual "Invoice(invoice-id, our company, 500.0)"

    // for classes that don't implement Show typeclass we will receive error during compilation
  }

}

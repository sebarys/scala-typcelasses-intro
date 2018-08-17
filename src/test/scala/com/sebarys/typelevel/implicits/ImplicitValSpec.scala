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
package com.sebarys.typelevel.implicits

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext

class ImplicitValSpec extends FlatSpec with Matchers {

  behavior of "implicits"

  it should "return implicit value defined for given type" in {

    //given
    implicit val implicitString: String = "implicit string"

    // we are importing implicit lazy val global: ExecutionContext
    import scala.concurrent.ExecutionContext.Implicits.global

    //when
    val receivedImplicitStringVal = implicitly[String]
    val receivedImplicitExecutionContextVal = implicitly[ExecutionContext]

    //then
    receivedImplicitStringVal shouldEqual implicitString
    receivedImplicitExecutionContextVal shouldEqual scala.concurrent.ExecutionContext.Implicits.global
  }

  it should "not compile if we want take value of implicit for given type not defined in scope" in {

    //if you will uncomment below line this Spec will not compile
    //val receivedImplicitStringVal = implicitly[String]
  }

  it should "compile if have more than one implicit value for given type but it is not used" in {

    //given
    implicit val firstImplicitString: String = "first implicit string"
    implicit val secondImplicitString: String = "second implicit string"

    //when

    //then
  }

  it should "not compile if have more than one implicit value for given type and want to take value" in {

    //given
    implicit val firstImplicitString: String = "first implicit string"
    implicit val secondImplicitString: String = "second implicit string"

    //when
    //if you will uncomment below line this Spec will not compile
    //val receivedImplicitStringVal = implicitly[String]

    //then
  }

}

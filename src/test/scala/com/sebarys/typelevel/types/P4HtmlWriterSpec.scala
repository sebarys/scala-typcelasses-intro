package com.sebarys.typelevel.types

import org.scalatest.{FlatSpec, Matchers}

class P4HtmlWriterSpec extends FlatSpec with Matchers {

  trait HtmlWriter[A] {
    def asHtml(value: A): String
  }

  object HtmlWriter {
    def apply[A: HtmlWriter]: HtmlWriter[A] = implicitly[HtmlWriter[A]]

    implicit class HtmlWriterOps[A: HtmlWriter](x: A) {
      def asHtml(): String = HtmlWriter[A].asHtml(x)
    }

  }

  /**
    * Exercise 1: Implement HTML writer that will allow to create HTML-like output for following classes
    * - Email - replace '@' with ' at '
    * - User - name should be uppercase and password should be replaced to '***'
    **/
  case class Email(address: String)

  case class User(name: String, email: Email, password: String)

  implicit val emailHtmlWriter = new HtmlWriter[Email] {
    override def asHtml(value: Email): String = ???
  }

  implicit val userHtmlWriter = new HtmlWriter[User] {
    override def asHtml(value: User): String = ???
  }

  behavior of "html writer"

  it should "change '@' to ' at ' in email addresses" in {
    //given
    import HtmlWriter._
    val email = Email("my.email@gmail.com")

    //when
    val emailAsHtml = email.asHtml()

    //then
    emailAsHtml shouldEqual "my.email at gmail.com"
  }

  it should "convert user to defined structure" in {
    //given
    import HtmlWriter._
    val email = Email("my.email@gmail.com")
    val user = User("username", email, "secretPassword")

    //when
    val userAsHtml = user.asHtml()

    //then
    userAsHtml shouldEqual "USERNAME, mail: my.email at gmail.com, password: ***"
  }

  /**
    * Exercise 2: Implement HTML writer that will allow to create HTML-like output for following classes
    * - Bold - value should be between <b></b> tags
    * - Paragraph - value should be between <p></p> tags
    **/

  case class Bold[A: HtmlWriter](nestedComponents: List[A])

  case class Paragraph[A: HtmlWriter](nestedComponents: List[A])

  implicit def listHtmlWriter[A: HtmlWriter] = new HtmlWriter[List[A]] {
    override def asHtml(value: List[A]): String = ???
  }

  implicit def boldHtmlWriter[A: HtmlWriter] = new HtmlWriter[Bold[A]] {
    override def asHtml(value: Bold[A]): String = ???
  }

  implicit def paragraphHtmlWriter[A: HtmlWriter] = new HtmlWriter[Paragraph[A]] {
    override def asHtml(value: Paragraph[A]): String = ???
  }

  it should "allow to use defined impl of HtmlWriter in Bold component" in {
    //given
    import HtmlWriter._
    val email1 = Email("my.email@gmail.com")
    val email2 = Email("other.email@gmail.com")
    val user1 = User("someUser1", email1, "secretPassword")
    val user2 = User("someUser2", email2, "topSecret")
    val boldedUsers: Bold[User] = Bold(List(user1, user2))

    //when
    val boldAsHtml = boldedUsers.asHtml()

    //then
    boldAsHtml shouldEqual
      """<b>[
        |SOMEUSER1, mail: my.email at gmail.com, password: ***
        |SOMEUSER2, mail: other.email at gmail.com, password: ***
        |]</b>""".stripMargin
  }


  it should "allow to use defined impl of HtmlWriter in Text component" in {
    //given
    import HtmlWriter._
    val email1 = Email("my.email@gmail.com")
    val email2 = Email("other.email@gmail.com")
    val user1 = User("someUser1", email1, "secretPassword")
    val user2 = User("someUser2", email2, "topSecret")
    val usersParagraph: Paragraph[User] = Paragraph(List(user1, user2))

    //when
    val paragrapAsHtml = usersParagraph.asHtml()

    //then
    paragrapAsHtml shouldEqual
      """<p>[
        |SOMEUSER1, mail: my.email at gmail.com, password: ***
        |SOMEUSER2, mail: other.email at gmail.com, password: ***
        |]</p>""".stripMargin
  }


}

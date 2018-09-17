package com.sebarys.typelevel.implicits

import com.sebarys.typelevel.implicits.OurDomain._
import org.scalatest.{FlatSpec, Matchers}

class ImplicitParametersSpec extends FlatSpec with Matchers {

  behavior of "implicit parameters"

  it should "allow to inject proper values to class when it declare implicit fields" in {

    //given

    //some initial values
    implicit val clientId: ClientId = ClientId("someClientId")
    implicit val clientSecret: ClientSecret = ClientSecret("someClientSecret")
    implicit val databaseAddress: DatabaseAddress = DatabaseAddress("10.55.223.12")

    //required instances, compiler based on types will inject required fields
    implicit val httpClient: HttpClient = new HttpClient()
    implicit val userRepository: UserRepository = new UserRepository()
    val userService: UserService = new UserService()

    val userToCreate = User(Name("Firstname"), Surname("Secondname"))
    val expectedResult = Result(s"$userToCreate created successfully")

    //when
    val result: Result = userService.create(userToCreate)

    //then
    result shouldEqual expectedResult
  }

  it should "allow to use implicit values in methods" in {
    //given
    val thisScopePrefix = "somePrefix"
    val testInput = "someTestInput"
    val expectedResult = thisScopePrefix + testInput
    implicit val prefixerInThisScope: Prefixer = Prefixer(thisScopePrefix)
    val stringPrefixer = new StringPrefixer

    //when
    val result: String = stringPrefixer.addPrefix(testInput)

    //then
    result shouldEqual expectedResult
  }
}

object OurDomain {

  // Value classes are a mechanism in Scala to avoid allocating runtime objects.
  // If we use this class as plain value (do not patter match, extend, define equals/hasCode, provide extra constructors etc.)
  // on compile time it be plain value.
  case class ClientId(value: String) extends AnyVal
  case class ClientSecret(value: String) extends AnyVal
  case class DatabaseAddress(value: String) extends AnyVal
  case class Name(value: String) extends AnyVal
  case class Surname(value: String) extends AnyVal
  case class User(name: Name, username: Surname)
  case class Result(value: String) extends AnyVal

  // service which calls repo that "use" http client
  class HttpClient(implicit clientId: ClientId, clientSecret: ClientSecret)

  class UserRepository(implicit httpClient: HttpClient, databaseAddress: DatabaseAddress) {
    def create(user: User): Result = {
      Result(s"$user created successfully")
    }
  }

  class UserService(implicit userRepository: UserRepository) {
    def create(user: User): Result = {
      userRepository.create(user)
    }
  }

  // prefixer and class with method that take implicit param
  case class Prefixer(valueToPrefix: String)

  class StringPrefixer {
    def addPrefix(input: String)(implicit prefixer: Prefixer): String = {
      prefixer.valueToPrefix + input
    }
  }

}

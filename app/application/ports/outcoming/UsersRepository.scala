package application.ports.outcoming

import domain.models.UserEntity

trait UsersRepository {
  def getUsers(user: Option[String]): Seq[UserEntity]

  def verifyUser(username: String, password: String): Option[UserEntity]

  def findUser(username: String, password: String): UserEntity = ???

}

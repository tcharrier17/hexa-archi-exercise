package lunatech.services.ports

import lunatech.entities.UserEntity

trait UsersRepository {
  def getUsers(user: Option[String]): Seq[UserEntity]

  def verifyUser(username: String, password: String): Option[UserEntity]

  def findUser(username: String, password: String): UserEntity = ???

}

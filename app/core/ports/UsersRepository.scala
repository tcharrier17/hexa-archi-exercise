package core.ports

import core.domain.models.UserEntity

import scala.concurrent.Future

trait UsersRepository {
  def createUser(userEntity: UserEntity): Boolean

  def createUsers(userEntities: Seq[UserEntity]): Boolean = {
    userEntities.forall(createUser)
  }

  def getUsers(user: Option[String]): Seq[UserEntity]

  def getUserById(id: String): UserEntity

  def getUserByUsername(username: String): Option[UserEntity]

  def updateUser(userEntity: UserEntity): Boolean

  def deleteUser(id: String): Boolean

  def deleteALl(): Boolean
}

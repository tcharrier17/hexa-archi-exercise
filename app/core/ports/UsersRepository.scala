package core.ports

import core.domain.models.UserEntity

trait UsersRepository {
  def createUser(userEntity: UserEntity): Boolean

  def createUsers(userEntities: Seq[UserEntity]): Boolean = {
    userEntities.forall(createUser)
  }

  def getUsers(user: Option[String]): Seq[UserEntity]

  def getUserById(id: String): UserEntity

  def getUserByUsername(username: String): UserEntity

  def updateUser(userEntity: UserEntity): Boolean

  def deleteUser(id: String): Boolean

  def deleteALl(): Boolean

  def verifyUser(username: String, password: String): Option[UserEntity]
}

package core.services

import core.domain.models.UserEntity
import core.ports.UsersRepository

import javax.inject.Inject
import scala.concurrent.Future

class UsersService @Inject()(usersRepository: UsersRepository) {
  def getUsers(user: Option[String]): Seq[UserEntity] = {
    usersRepository.getUsers(user)
  }

  def createUser(userEntity: UserEntity): Boolean = {
    usersRepository.createUser(userEntity)
  }

  def getUserById(id: String): UserEntity = {
    usersRepository.getUserById(id)
  }

  def getUserByUsername(username: String): Option[UserEntity] = {
    usersRepository.getUserByUsername(username)
  }

  def updateUser(userEntity: UserEntity): Boolean = {
    usersRepository.updateUser(userEntity)
  }

  def deleteUser(id: String): Boolean = {
    usersRepository.deleteUser(id)
  }
}

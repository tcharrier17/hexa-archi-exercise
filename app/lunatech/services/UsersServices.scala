package lunatech.services

import lunatech.entities.UserEntity
import lunatech.services.ports.UsersRepository

import javax.inject.Inject

class UsersServices @Inject() (usersRepository: UsersRepository) {
  def getUsers(user: Option[String]): Seq[UserEntity] = {
    usersRepository.getUsers(user)
  }
}

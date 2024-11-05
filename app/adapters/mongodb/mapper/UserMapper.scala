package adapters.mongodb.mapper

import adapters.mongodb.models.UserDocument
import domain.models
import domain.models.UserEntity

object UserMapper {
  def userMapper(userDocument: UserDocument): UserEntity = {
    models.UserEntity(userDocument.username, userDocument.password, userDocument.role, userDocument.todos.map(todo =>  TodoMapper.todoMapper(todo)))
  }
}
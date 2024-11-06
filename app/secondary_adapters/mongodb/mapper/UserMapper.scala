package secondary_adapters.mongodb.mapper

import core.domain.models
import core.domain.models.UserEntity
import secondary_adapters.mongodb.models.UserDocument

object UserMapper {
  def userMapper(userDocument: UserDocument): UserEntity = {
    models.UserEntity(userDocument.username, userDocument.password, userDocument.role, userDocument.todos.map(todo =>  TodoMapper.todoMapper(todo)))
  }
}
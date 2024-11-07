package secondary_adapters.mongodb.mapper

import core.domain.models
import core.domain.models.UserEntity
import secondary_adapters.mongodb.models.UserDocument

object UserMapper {
  def userMapper(userDocument: UserDocument): UserEntity = {
    UserEntity(userDocument.username, userDocument.password, userDocument.role, userDocument.todos.map(todo =>  TodoMapper.todoMapper(todo)))
  }

  def userDocumentMapper(userEntity: UserEntity): UserDocument = {
    UserDocument(userEntity.username, userEntity.password, userEntity.role, userEntity.todos.map(todo =>  TodoMapper.todoDocumentMapper(todo)))
  }
}
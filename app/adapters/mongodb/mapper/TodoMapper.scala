package adapters.mongodb.mapper

import adapters.mongodb.models.TodoDocument
import domain.models.TodoEntity

object TodoMapper {
  def todoMapper(todoDocument: TodoDocument): TodoEntity = {
    TodoEntity(todoDocument._id.toString, todoDocument.title, todoDocument.tags, todoDocument.done)
  }

  def todoDocumentMapper(todoEntity: TodoEntity): TodoDocument = {
    todoEntity.id match {
      case "" => TodoDocument(todoEntity.title, todoEntity.tags, todoEntity.done)
      case _ => TodoDocument(todoEntity.id ,todoEntity.title, todoEntity.tags, todoEntity.done)
    }
  }
}

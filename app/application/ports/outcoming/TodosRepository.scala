package application.ports.outcoming

import adapters.mongodb.models.UserDocument
import domain.models.TodoEntity

import scala.concurrent.Future

trait TodosRepository {

  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity]

  def getTodo(id: String, owner: Option[String]): TodoEntity

  def createTodo(todo: TodoEntity, username: String): Boolean

  def updateTodo(todoEntity: TodoEntity, username: String): Boolean

  def deleteTodo(id: String, username: String): Boolean

}

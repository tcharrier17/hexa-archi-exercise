package core.ports

import core.domain.models.TodoEntity

import scala.concurrent.Future

trait TodosRepository {

  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Future[Seq[TodoEntity]]

  def getTodo(id: String, owner: Option[String]): Future[TodoEntity]

  def createTodo(todo: TodoEntity, username: String): Future[String]

  def updateTodo(todoEntity: TodoEntity, username: String): Future[String]

  def deleteTodo(id: String, username: String): Future[String]

}

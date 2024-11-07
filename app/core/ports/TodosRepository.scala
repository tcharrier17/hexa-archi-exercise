package core.ports

import core.domain.models.TodoEntity

trait TodosRepository {

  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity]

  def getTodo(id: String, owner: Option[String]): TodoEntity

  def createTodo(todo: TodoEntity, username: String): Boolean

  def updateTodo(todoEntity: TodoEntity, username: String): Boolean

  def deleteTodo(id: String, username: String): Boolean

}

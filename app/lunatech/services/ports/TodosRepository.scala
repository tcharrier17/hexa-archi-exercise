package lunatech.services.ports

import lunatech.entities.TodoEntity

trait TodosRepository {
  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity]

  def getTodo(id: String, owner: Option[String]): TodoEntity

  def createTodo(todo: TodoEntity): Boolean

  def updateTodo(id: String): Boolean

  def deleteTodo(id: String): Boolean

}

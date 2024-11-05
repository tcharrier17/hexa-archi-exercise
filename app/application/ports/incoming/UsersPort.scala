package application.ports.incoming

import domain.models.{TodoEntity, UserEntity}

trait UsersPort {
  def getUsers(user: Option[String]): Seq[UserEntity]

  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity]

  def getTodo(id: String, owner: Option[String]): TodoEntity

  def createTodo(todo: TodoEntity, username: String): Boolean

  def updateTodo(todoEntity: TodoEntity, username: String): Boolean

  def deleteTodo(id: String, username: String): Boolean
}

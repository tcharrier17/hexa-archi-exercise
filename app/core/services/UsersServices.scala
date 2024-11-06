package core.services

import core.domain.models.{TodoEntity, UserEntity}
import core.ports.incoming.UsersPort
import core.ports.outcoming.{TodosRepository, UsersRepository}

import javax.inject.Inject

class UsersServices @Inject() (usersRepository: UsersRepository, todosRepository: TodosRepository) extends UsersPort {
  def getUsers(user: Option[String]): Seq[UserEntity] = {
    usersRepository.getUsers(user)
  }

  override def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity] = {
    todosRepository.getTodos(tagsFilter, owner)
  }

  override def getTodo(id: String, owner: Option[String]): TodoEntity = {
    todosRepository.getTodo(id, owner)
  }

  override def createTodo(todo: TodoEntity, username: String): Boolean = {
    todosRepository.createTodo(todo, username)
  }


  override def updateTodo(todoEntity: TodoEntity, username: String): Boolean = {
    todosRepository.updateTodo(todoEntity, username)
  }

  override def deleteTodo(id: String, username: String): Boolean = {
    todosRepository.deleteTodo(id, username)
  }
}

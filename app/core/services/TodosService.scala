package core.services

import core.domain.models.TodoEntity
import core.ports.{TodosRepository, UsersRepository}

import javax.inject.Inject

class TodosService @Inject()(todosRepository: TodosRepository) {
  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity] = {
    todosRepository.getTodos(tagsFilter, owner)
  }

  def getTodo(id: String, owner: Option[String]): TodoEntity = {
    todosRepository.getTodo(id, owner)
  }

  def createTodo(todo: TodoEntity, username: String): Boolean = {
    todosRepository.createTodo(todo, username)
  }

  def updateTodo(todoEntity: TodoEntity, username: String): Boolean = {
    todosRepository.updateTodo(todoEntity, username)
  }

  def deleteTodo(id: String, username: String): Boolean = {
    todosRepository.deleteTodo(id, username)
  }
}

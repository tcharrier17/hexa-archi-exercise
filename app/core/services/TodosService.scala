package core.services

import core.domain.models.TodoEntity
import core.ports.{TodosRepository, UsersRepository}

import javax.inject.Inject
import scala.concurrent.Future

class TodosService @Inject()(todosRepository: TodosRepository) {
  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Future[Seq[TodoEntity]] = {
    todosRepository.getTodos(tagsFilter, owner)
  }

  def getTodo(id: String, owner: Option[String]): Future[TodoEntity] = {
    todosRepository.getTodo(id, owner)
  }

  def createTodo(todo: TodoEntity, username: String): Future[String] = {
    todosRepository.createTodo(todo, username)
  }

  def updateTodo(todoEntity: TodoEntity, username: String): Future[String] = {
    todosRepository.updateTodo(todoEntity, username)
  }

  def deleteTodo(id: String, username: String): Future[String] = {
    todosRepository.deleteTodo(id, username)
  }
}

package lunatech.services

import lunatech.entities.{TodoEntity, UserEntity}
import lunatech.services.ports.{TodosRepository, UsersRepository}

import javax.inject.Inject

class TodosServices @Inject()(todosRepository: TodosRepository) {
  def getTodos(tagsFilter: Option[List[String]], owner: Option[String]): Seq[TodoEntity] = {
    todosRepository.getTodos(tagsFilter, owner)
  }

  def getTodo(id: String, owner: Option[String]): TodoEntity = {
    todosRepository.getTodo(id, owner)
  }

  def createTodo(todo: TodoEntity): Boolean = {
    todosRepository.createTodo(todo)
  }
}

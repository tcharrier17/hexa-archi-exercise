package core.domain.models

case class TodoEntity(id: String = "", title: String, tags: Seq[String], done: Boolean)
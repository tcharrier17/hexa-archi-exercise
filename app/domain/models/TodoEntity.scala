package domain.models

import play.api.libs.json.{Json, OFormat, Writes}

case class TodoEntity(id: String = "", title: String, tags: Seq[String], done: Boolean)

object TodoEntity {

  implicit val todoJson: Writes[TodoEntity] = Json.writes[TodoEntity]

  implicit val jsonUser: OFormat[TodoEntity] = Json.format[TodoEntity]

}

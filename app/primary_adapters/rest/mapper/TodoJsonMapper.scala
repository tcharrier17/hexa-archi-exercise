package primary_adapters.rest.mapper

import core.domain.models.TodoEntity
import play.api.libs.json.{JsValue, Json, OFormat, Writes}

object TodoJsonMapper {
    implicit val todoJson: Writes[TodoEntity] = Json.writes[TodoEntity]

    implicit val jsonTodo: OFormat[TodoEntity] = Json.format[TodoEntity]

    def toJson(todoEntity: TodoEntity): JsValue = Json.toJson(todoEntity)

    def toJsonList(todoEntities: Seq[TodoEntity]): JsValue = Json.toJson(todoEntities)

    def fromJson(json: JsValue): Option[TodoEntity] = json.validate[TodoEntity].asOpt
}

package primary_adapters.rest.mapper

import core.domain.models.UserEntity
import play.api.libs.json.{JsValue, Json, OFormat, Writes}
import primary_adapters.rest.mapper.TodoJsonMapper.{todoJson, jsonTodo}

object UserJsonMapper {
  implicit val userJson: Writes[UserEntity] = Json.writes[UserEntity]

  implicit val jsonUser: OFormat[UserEntity] = Json.format[UserEntity]

  def toJson(userEntity: UserEntity): JsValue = Json.toJson(userEntity)

  def toJsonList(userEntities: Seq[UserEntity]): JsValue = Json.toJson(userEntities)

  def fromJson(json: JsValue): Option[UserEntity] = json.validate[UserEntity].asOpt
}

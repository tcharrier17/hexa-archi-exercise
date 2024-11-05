package domain.models

import org.mongodb.scala.bson.ObjectId

import java.time.ZonedDateTime

case class CommandEntity(_id: ObjectId, ts: ZonedDateTime, basket: Map[String, Integer], totalPrice: Integer)

object CommandEntity {
  def apply(ts: ZonedDateTime, basket: Map[String, Integer], totalPrice: Integer): CommandEntity =
    CommandEntity(new ObjectId(), ts, basket, totalPrice)
}
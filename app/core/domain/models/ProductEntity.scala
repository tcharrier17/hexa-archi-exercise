package core.domain.models

import org.mongodb.scala.bson.ObjectId

case class ProductEntity(_id: ObjectId, name: String, basket: Map[String, Integer])

object ProductEntity {
  def apply(name: String, basket: Map[String, Integer]): ProductEntity =
    ProductEntity(new ObjectId, name, basket)
}
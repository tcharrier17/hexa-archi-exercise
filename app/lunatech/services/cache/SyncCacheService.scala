package lunatech.services.cache

import play.api.cache.SyncCacheApi

import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class SyncCacheService extends SyncCacheApi {

  var listObjectCached = List.empty[(String, Any)]

  override def set(key: String, value: Any, expiration: Duration): Unit = {
    listObjectCached ++= List((key, value))
  }

  override def remove(key: String): Unit = {
    listObjectCached = listObjectCached.filter(_._1 != key)
  }

  override def getOrElseUpdate[A: ClassTag](key: String, expiration: Duration)(orElse: => A): A = {
    ???
  }

  override def get[T: ClassTag](key: String): Option[T] = {
    // Trouve l'élément correspondant à la clé
    listObjectCached.find(_._1 == key) match {
      case Some((_, value)) =>
        // Vérifie que la valeur est bien du type attendu
        Option(value).collect { case v if implicitly[ClassTag[T]].runtimeClass.isInstance(v) =>
          v.asInstanceOf[T]
        }
      case None => None
    }
  }
}

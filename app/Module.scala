import adapters.models.{MongoTodosAccess, MongoUserAccess}
import com.google.inject.AbstractModule
import lunatech.services.ports.{TodosRepository, UsersRepository}
import play.api.cache.AsyncCacheApi
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UsersRepository]).to(classOf[MongoUserAccess])
    bind(classOf[TodosRepository]).to(classOf[MongoTodosAccess])
  }
}

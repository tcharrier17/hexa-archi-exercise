import com.google.inject.AbstractModule
import core.ports.{TodosRepository, UsersRepository}
import play.api.{Configuration, Environment}
import secondary_adapters.mongodb.repository.{MongoTodosAccess, MongoUsersRepository}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UsersRepository]).to(classOf[MongoUsersRepository])
    bind(classOf[TodosRepository]).to(classOf[MongoTodosAccess])
  }
}

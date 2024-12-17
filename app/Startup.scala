import com.google.inject.Singleton
import core.domain.models.UserEntity
import core.ports.UsersRepository
import core.resources.Role
import play.api.Logging
import play.api.inject._

import javax.inject._
import scala.concurrent.Future

/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 */
@Singleton
class Startup @Inject()(userRepository: UsersRepository, lifecycle: ApplicationLifecycle) extends Logging {
  val users: Seq[UserEntity] = List(
    UserEntity("Nicolas", "pwd", Role.ADMIN),
    UserEntity("Ewen", "pwd", Role.REGULAR),
    UserEntity("Sebastien", "pwd", Role.REGULAR)
  )


  private def loadFixtures(): Unit = {
    logger.info("Executing fixtures startup operation")
    println("Startup OK")
    userRepository.deleteALl()
    userRepository.createUsers(users)
  }

  loadFixtures()

  // Ajoute un hook pour nettoyer à la fermeture
  lifecycle.addStopHook { () =>
    Future.successful(logger.info("Stopping application and cleaning up resources"))
  }

}

/*
@Singleton
public class Startup {
    private static final Logger logger = Logger.getLogger(Startup.class);

    private final List<UserEntity> users = List.of(
            new UserEntity("Nicolas", "pwd", Role.ADMIN, new ArrayList<>()),
            new UserEntity("Ewen", "pwd", Role.REGULAR, new ArrayList<>()),
            new UserEntity("Sebastien", "pwd", Role.REGULAR, new ArrayList<>())
    );

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");

        UserEntity.deleteAll();
        users.forEach(u -> u.persist());
    }
}*/
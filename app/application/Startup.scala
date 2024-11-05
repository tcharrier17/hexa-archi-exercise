package application

import com.google.inject.Singleton
import application.resources.Role
import domain.models.UserEntity
import play.api.Logging

import javax.inject._
import javax.transaction._

/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 */
@Singleton
class Startup @Inject() extends Logging {
//  val users = List(
//    new UserEntity("Nicolas", "pwd", Role.ADMIN, List.empty),
//    new UserEntity("Ewen", "pwd", Role.REGULAR, List.empty),
//    new UserEntity("Sebastien", "pwd", Role.REGULAR, List.empty)
//  )

//  @Transactional
//  def loadFixtures(@Observes evt: StartupEvent): Unit = {
//    logger.info("Executing fixtures startup operation")
//
//    UserEntity.deleteAll()
//    users.foreach(_.persist())
//  }

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
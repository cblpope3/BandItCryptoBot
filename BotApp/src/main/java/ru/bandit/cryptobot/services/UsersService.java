package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.UserEntity;
import ru.bandit.cryptobot.exceptions.CommonBotAppException;
import ru.bandit.cryptobot.exceptions.UserException;
import ru.bandit.cryptobot.repositories.UsersRepository;

import java.sql.Timestamp;

/**
 * This service implements interaction with users.
 */
@Service
public class UsersService {

    private final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Process start command for given user. If user not yet registered, then add new user to database.
     * If user with given chatId already exist - just increment start counter.
     *
     * @param user user data as {@link UserDTO}.
     */
    public void startUser(UserDTO user) {

        UserEntity startingUser = usersRepository.findByUserId(user.getUserId());

        if (startingUser != null) {

            //updating number of /start command counter
            if (startingUser.getStartCount() == null) startingUser.setStartCount(1L);
            else startingUser.setStartCount(startingUser.getStartCount() + 1);

            //updating chat id and last message id
            startingUser.setChatId(user.getChatId());
            startingUser.setLastMessageId(user.getLastMessageId());

            usersRepository.save(startingUser);

            logger.trace("User {} already in database.", user.getUserId());
        } else {
            this.saveNewUser(user);
        }
    }

    /**
     * Method to set all users subscriptions paused.
     *
     * @param user user as {@link UserDTO}.
     * @throws CommonBotAppException if subscriptions are already in pause.
     */
    public void pauseSubscriptions(UserDTO user) throws CommonBotAppException {
        UserEntity foundUser = getUserEntity(user);

        if (foundUser.isPaused()) {
            if (logger.isTraceEnabled()) logger.trace("User #{} already paused.", user.getUserId());
            throw new UserException("Subscriptions already paused.", UserException.ExceptionCause.ALREADY_PAUSED);
        }

        foundUser.setPaused(true);
        usersRepository.save(foundUser);
        if (logger.isTraceEnabled())
            logger.trace("Subscriptions for user #{} successfully paused.", user.getUserId());

    }

    /**
     * Method to set all users subscriptions resumed.
     *
     * @param user user as {@link UserDTO}.
     * @throws CommonBotAppException if subscriptions already resumed.
     */
    public void resumeSubscriptions(UserDTO user) throws CommonBotAppException {
        UserEntity foundUser = getUserEntity(user);

        if (!foundUser.isPaused()) {
            if (logger.isTraceEnabled()) logger.trace("User #{} already resumed.", user.getUserId());
            throw new UserException("Subscriptions already resumed.", UserException.ExceptionCause.ALREADY_RESUMED);
        }

        foundUser.setPaused(false);
        usersRepository.save(foundUser);
        if (logger.isTraceEnabled())
            logger.trace("Subscriptions for user #{} successfully resumed.", user.getUserId());
    }

    /**
     * Check subscription status of user.
     *
     * @param user user as {@link UserDTO}.
     * @return true if user subscriptions paused.
     */
    public boolean isUserPaused(UserDTO user) {
        return getUserEntity(user).isPaused();
    }

    /**
     * Save new user in database.
     *
     * @param user new user to save as {@link UserDTO}.
     * @return saved {@link UserEntity}.
     */
    private UserEntity saveNewUser(UserDTO user) {

        UserEntity newUser = user.generateNewUser();

        newUser.setStartCount(1L);

        newUser.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("Creating new user #{}.", newUser.getUserId());

        return usersRepository.save(newUser);
    }

    /**
     * Get {@link UserEntity} by {@link UserDTO}.
     *
     * @param user user as {@link UserDTO}.
     * @return found {@link UserEntity}. If requested user is not found in database, new user is saved.
     */
    public UserEntity getUserEntity(UserDTO user) {
        UserEntity foundUser = usersRepository.findByUserId(user.getUserId());
        if (foundUser == null) {
            foundUser = this.saveNewUser(user);
            logger.trace("User #{} not found.", user.getUserId());
        }
        return foundUser;
    }
}

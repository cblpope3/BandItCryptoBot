package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.dto.UserDTO;
import ru.bandit.cryptobot.entities.UserEntity;
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
     * @return true if new user was saved. false if user is already in database.
     */
    public boolean startUser(UserDTO user) {

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
            return false;

        } else {
            this.saveNewUser(user);
            return true;
        }
    }

    /**
     * Method to set all users subscriptions paused.
     *
     * @param user user as {@link UserDTO}.
     * @deprecated use {@link #inverseSubscriptionsPause(UserDTO)} instead
     */
    @Deprecated(forRemoval = false)
    public void pauseSubscriptions(UserDTO user) {
        UserEntity foundUser = usersRepository.findByUserId(user.getUserId());
        if (foundUser == null) {
            logger.warn("Unregistered user #{} trying to pause subscription.", user.getUserId());
            saveNewUser(user, true);
        } else {
            foundUser.setPaused(true);
            usersRepository.save(foundUser);
            if (logger.isTraceEnabled())
                logger.trace("Subscriptions for user #{} successfully paused.", user.getUserId());
        }
    }

    /**
     * Method to set all users subscriptions resumed.
     *
     * @param user user as {@link UserDTO}.
     * @deprecated use {@link #inverseSubscriptionsPause(UserDTO)} instead
     */
    @Deprecated(forRemoval = false)
    public void resumeSubscriptions(UserDTO user) {
        UserEntity foundUser = usersRepository.findByUserId(user.getUserId());
        if (foundUser == null) {
            logger.warn("Unregistered user #{} trying to resume subscription.", user.getUserId());
            saveNewUser(user, true);
        } else {
            foundUser.setPaused(false);
            usersRepository.save(foundUser);
            if (logger.isTraceEnabled())
                logger.trace("Subscriptions for user #{} successfully resumed.", user.getUserId());
        }
    }

    /**
     * Switch users subscriptions pause mode to opposite state.
     *
     * @param user user as {@link UserDTO}.
     */
    public void inverseSubscriptionsPause(UserDTO user) {
        UserEntity foundUser = usersRepository.findByUserId(user.getUserId());
        if (foundUser == null) {
            logger.warn("Unregistered user #{} trying to switch subscription.", user.getUserId());
            saveNewUser(user);
        } else {
            //inverting subscriptions
            foundUser.setPaused(!foundUser.isPaused());
            usersRepository.save(foundUser);
            if (logger.isTraceEnabled())
                logger.trace("Subscriptions for user #{} successfully switched to {}.", user.getUserId(), foundUser.isPaused());
        }
    }

    /**
     * Check subscription status of user.
     *
     * @param user user as {@link UserDTO}.
     * @return true if user subscriptions paused.
     */
    public boolean isUserPaused(UserDTO user) {
        return usersRepository.findByUserId(user.getUserId()).isPaused();
    }

    /**
     * Save new user in database with resumed subscriptions by default.
     *
     * @param user new user to save as {@link UserDTO}.
     */
    private void saveNewUser(UserDTO user) {
        saveNewUser(user, false);
    }


    /**
     * Save new user in database.
     *
     * @param user     new user to save as {@link UserDTO}.
     * @param isPaused is new user's subscriptions paused.
     */
    private void saveNewUser(UserDTO user, boolean isPaused) {

        UserEntity newUser = user.generateNewUser();

        newUser.setPaused(isPaused);
        newUser.setStartCount(1L);

        newUser.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        logger.debug("Creating new user {}.", newUser.getUserId());
        usersRepository.save(newUser);
    }

    /**
     * Get user by chat id.
     *
     * @param user user as {@link UserDTO}.
     * @return found user entity as {@link UserEntity}. If user is not found returns null.
     */
    public UserEntity getUser(UserDTO user) {
        UserEntity foundUser = usersRepository.findByUserId(user.getUserId());
        if (foundUser == null) {
            logger.warn("User {} not found", user.getUserId());
        }
        return foundUser;
    }
}

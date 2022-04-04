package ru.bandit.cryptobot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
     * @param chatId   users chat id.
     * @param username users first name as specified in profile.
     * @return true if new user was saved. false if user is already in database.
     */
    public boolean startUser(Long chatId, String username) {

        //TODO use userDto class to exchange it between methods instead of chatId.

        //input params null check
        if (chatId == null || username == null) {
            logger.error("Null in input parameters: chatId - {}, username - {}.", chatId, username);
            return false;
        }


        UserEntity newChat = usersRepository.findByChatId(chatId);

        if (newChat != null) {

            //updating number of /start command counter
            if (newChat.getStartCount() == null) newChat.setStartCount(1L);
            else newChat.setStartCount(newChat.getStartCount() + 1);
            usersRepository.save(newChat);

            logger.trace("User {} already in chat.", chatId);
            return false;

        } else {
            this.saveNewUser(chatId, username);
            return true;
        }
    }

    //todo decide if better switch subscriptions status

    /**
     * Method to set all users subscriptions paused.
     *
     * @param chatId telegram chat id.
     * @return true if subscriptions paused successfully. False if user not found.
     */
    @Deprecated(forRemoval = false)
    public boolean pauseSubscriptions(Long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            //todo if user not found, register him
            logger.warn("Can't pause user subscription because user {} not found.", chatId);
            return false;
        } else {
            user.setPaused(true);
            usersRepository.save(user);
            logger.trace("Subscriptions for user {} successfully paused.", chatId);
            return true;
        }
    }

    //todo decide if better switch subscriptions status

    /**
     * Method to set all users subscriptions resumed.
     *
     * @param chatId telegram chat id.
     * @return true if subscriptions paused successfully. False if user not found.
     */
    @Deprecated(forRemoval = false)
    public boolean resumeSubscriptions(Long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            //todo if user not found, register him
            logger.warn("Can't resume user subscription because user {} not found.", chatId);
            return false;
        } else {
            user.setPaused(false);
            usersRepository.save(user);
            logger.trace("Subscriptions for user {} successfully paused.", chatId);
            return true;
        }
    }

    /**
     * Switch users subscriptions pause mode to opposite state.
     *
     * @param chatId telegram chat id.
     * @return true if user was found. false otherwise.
     */
    public boolean inverseSubscriptionsPause(Long chatId) {
        UserEntity user = usersRepository.findByChatId(chatId);
        if (user == null) {
            //todo if user not found, register him
            logger.warn("Can't invert user subscription because user {} not found.", chatId);
            return false;
        } else {
            //inverting subscriptions
            user.setPaused(!user.isPaused());
            usersRepository.save(user);
            logger.trace("Subscriptions for user {} successfully switched to {}.", chatId, user.isPaused());
            return true;
        }
    }

    /**
     * Check subscription status of user.
     *
     * @param chatId telegram chat id.
     * @return true if user subscriptions paused.
     */
    public boolean isUserPaused(Long chatId) {
        return usersRepository.findByChatId(chatId).isPaused();
    }

    /**
     * Save new user in database.
     *
     * @param chatId   telegram chat id.
     * @param username first name of user.
     */
    private void saveNewUser(Long chatId, String username) {

        UserEntity newUser = new UserEntity();
        newUser.setChatId(chatId);
        newUser.setChatName(username);
        newUser.setPaused(false);
        newUser.setStartCount(1L);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        newUser.setRegistrationDate(timestamp);
        usersRepository.save(newUser);
        logger.debug("Created new user {}.", chatId);
    }

    /**
     * Get user by chat id.
     *
     * @param chatId telegram chatId.
     * @return found user entity as {@link UserEntity}. If user is not found returns null.
     */
    public UserEntity getUser(Long chatId) {
        UserEntity foundUser = usersRepository.findByChatId(chatId);
        if (foundUser == null) {
            logger.warn("User {} not found", chatId);
        }
        return foundUser;
    }
}

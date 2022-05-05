package ru.bandit.cryptobot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bandit.cryptobot.entities.MetricsEntity;
import ru.bandit.cryptobot.repositories.MetricsRepository;

/**
 * This service implements methods that handles actions with different metrics.
 *
 * @see #incrementTextCommandCounter()
 * @see #incrementInteractiveCommandCounter()
 * @see #incrementHelpCommandCounter()
 * @see #getHelpCommandCount()
 * @see #getTextCommandCount()
 * @see #getInteractiveCommandCount()
 * @see MetricsEntity
 */
@Service
@SuppressWarnings("unused")
public class MetricsService {

    private final MetricsRepository metricsRepository;

    @Autowired
    public MetricsService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    /**
     * Method increments counter for text command using.
     */
    public void incrementTextCommandCounter() {
        MetricsEntity metrics = this.getMetrics();
        if (metrics == null) metrics = new MetricsEntity();
        metrics.setTextCommandCount(metrics.getTextCommandCount() + 1);
        metricsRepository.save(metrics);
    }

    /**
     * Method increment counter for interactive command using.
     */
    public void incrementInteractiveCommandCounter() {
        MetricsEntity metrics = this.getMetrics();
        if (metrics == null) metrics = new MetricsEntity();
        metrics.setInteractiveCommandCount(metrics.getInteractiveCommandCount() + 1);
        metricsRepository.save(metrics);
    }

    /**
     * Method increment counter for help command using.
     */
    public void incrementHelpCommandCounter() {
        MetricsEntity metrics = this.getMetrics();
        if (metrics == null) metrics = new MetricsEntity();
        metrics.setHelpCount(metrics.getHelpCount() + 1);
        metricsRepository.save(metrics);
    }

    /**
     * Method increment counter for donations. Not used in current version of application,
     * implemented for future use.
     */
    @SuppressWarnings("unused")
    public void incrementDonateCounter() {
        MetricsEntity metrics = this.getMetrics();
        if (metrics == null) metrics = new MetricsEntity();
        metrics.setDonateCount(metrics.getDonateCount() + 1);
        metricsRepository.save(metrics);
    }

    /**
     * Get help command counter value stored in database. Not used in current version of application,
     * implemented for future use.
     *
     * @see MetricsEntity
     */
    @SuppressWarnings("unused")
    public Long getHelpCommandCount() {
        return this.getMetrics().getHelpCount();
    }

    /**
     * Get text command counter value stored in database. Not used in current version of application,
     * implemented for future use.
     *
     * @see MetricsEntity
     */
    @SuppressWarnings("unused")
    public Long getTextCommandCount() {
        return this.getMetrics().getTextCommandCount();
    }

    /**
     * Get interactive command counter value stored in database. Not used in current version of application,
     * implemented for future use.
     *
     * @see MetricsEntity
     */
    @SuppressWarnings("unused")
    public Long getInteractiveCommandCount() {
        return this.getMetrics().getInteractiveCommandCount();
    }

    /**
     * Get donations counter value stored in database. Not used in current version of application,
     * implemented for future use.
     *
     * @see MetricsEntity
     */
    @SuppressWarnings("unused")
    public Long getDonationsCount() {
        return this.getMetrics().getDonateCount();
    }

    /**
     * Get single metrics record.
     *
     * @return metrics data.
     * @see MetricsEntity
     */
    private MetricsEntity getMetrics() {
        return metricsRepository.findById(1L);
    }
}

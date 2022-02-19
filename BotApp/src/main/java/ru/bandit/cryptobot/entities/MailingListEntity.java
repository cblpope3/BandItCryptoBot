package ru.bandit.cryptobot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mailing_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailingListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mailing_list_id_generator")
    @SequenceGenerator(name = "mailing_list_id_generator", sequenceName = "mailing_list_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    @Column(name = "currency", nullable = false)
    private String currency;
}

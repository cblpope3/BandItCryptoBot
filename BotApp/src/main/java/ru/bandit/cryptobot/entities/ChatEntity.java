package ru.bandit.cryptobot.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "active_chats")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @SequenceGenerator(name = "users_id_generator", sequenceName = "users_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    //TODO change to chat_id
    @Column(name = "chat_name")
    private Long chatName;
}
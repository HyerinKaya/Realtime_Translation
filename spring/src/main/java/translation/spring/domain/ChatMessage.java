package translation.spring.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatmessage")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String senderId;
    private String receiverId;
    private String chatId;
    private MessageStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDateTime;

    @ManyToOne
    @JoinColumn(name = "chatroomId")
    private ChatRoom chatRoom;


    public enum MessageStatus{
        RECEIVED, DELIVERED
    }


}



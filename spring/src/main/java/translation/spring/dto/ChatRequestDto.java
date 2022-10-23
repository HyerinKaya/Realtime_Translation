package translation.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import translation.spring.domain.ChatMessage;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ChatRequestDto {
    /**
     * 송신자 id
     */
    @NotNull
    private String senderId;

    /**
     * 수신자 id
     */
    @NotNull
    private String receiverId;

    /**
     * 채팅방 id
     */
    @NotNull
    private Long chatroomId;

    /**
     * 메시지 내용
     */
    @NotBlank
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendDateTime;

    public ChatRequestDto(String senderId, String receiverId, Long chatroomId, String content, LocalDateTime sendDateTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatroomId = chatroomId;
        this.content = content;
        this.sendDateTime = sendDateTime;
    }



    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .sendDateTime(sendDateTime)
                .build();
    }
}

package translation.spring.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import translation.spring.domain.ChatMessage;
import translation.spring.dto.ChatRequestDto;
import translation.spring.exception.ResourceNotFoundException;
import translation.spring.repository.ChatMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository chatMessageRepository;

    //수신한 채팅 저장
    public ChatMessage save(ChatRequestDto chatRequestDto){
        ChatMessage chatMessage = chatRequestDto.toEntity();
        chatMessage.setStatus(ChatMessage.MessageStatus.RECEIVED);

        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }


    //안 읽은 채팅 개수 표시
    @Override
    public long countNewMessages(String senderId, String receiverId) {
        return chatMessageRepository.countBySenderIdAndReceiverIdAndStatus(
                senderId, receiverId, ChatMessage.MessageStatus.RECEIVED);
    }


    //안 읽은 채팅 가져오기
    @Override
    public List<ChatMessage> findChatMessages(String senderId, String receiverId) {
        return null;
    }



    @Override
    public void updateStatuses(String senderId, String receiverId, ChatMessage.MessageStatus status) {

    }
}

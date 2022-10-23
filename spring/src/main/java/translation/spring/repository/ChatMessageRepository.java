package translation.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import translation.spring.domain.ChatMessage;

import java.util.List;


@Repository
@Component
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    long countBySenderIdAndReceiverIdAndStatus(
            String senderId, String receiverId, ChatMessage.MessageStatus status);

    @Query("select c from ChatMessage c where " +
            "(c.senderId=:senderId or c.senderId=:receiverId) " +
            "and (c.receiverId=:receiverId or c.receiverId=:senderId)")
    List<ChatMessage> findBySenderIdAndReceiverId(
            String senderId, String receiverId);


    //List<ChatMessage> findByChatroomId(Object chatId);

}

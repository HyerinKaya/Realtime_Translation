package translation.spring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import translation.spring.domain.ChatMessage;
import translation.spring.domain.ChatNotification;
import translation.spring.dto.ChatRequestDto;
import translation.spring.repository.ChatMessageRepository;
import translation.spring.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;

    /*
    TODO: 세션 등 자동으로 senderId 받아오기
     */
    @GetMapping("/api/chat/new/{senderId}/{receiverId}/{chatroomId}")
    public ResponseEntity<?> createChatForm(@PathVariable String senderId,
                                 @PathVariable String receiverId,
                                 @PathVariable Long chatroomId,
                                 Model model, HttpServletRequest request) throws Exception {
        /*
        TODO: 1)PathVariable들 프론트에서 처리할지, 2)채팅방 생성, 채팅 진입 분리할지
         */

        List<ChatMessage> chatMessageList =  chatMessageRepository.findBySenderIdAndReceiverId(senderId,receiverId);

        return new ResponseEntity<>(chatMessageList, HttpStatus.OK);
    }


    @MessageMapping("/chat")  //여기로 전송되면 메서드 호출
    public void processMessage(@Payload String msg) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ChatRequestDto chatRequestDto = mapper.readValue(msg, ChatRequestDto.class);
        chatRequestDto.setSendDateTime(LocalDateTime.now());

        System.out.print("메시지 저장  (수신자):"+chatRequestDto.getReceiverId());
        System.out.print(" / (채팅방아이디):"+chatRequestDto.getChatroomId());
        System.out.println(" / (content):"+chatRequestDto.getContent());


        //메시지 저장하기
        ChatMessage saved = chatMessageService.save(chatRequestDto);

        //수신 유저에게 메시지 수신 알림 보내기
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(chatRequestDto.getReceiverId()),"/user/{receiverId}/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId()));
    }
}
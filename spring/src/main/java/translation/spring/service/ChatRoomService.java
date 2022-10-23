package translation.spring.service;

import java.util.Optional;

public interface ChatRoomService {

    //채팅방 가져오기, 없을 시 신규 생성
    public Optional<String> getChatId(String senderId, String receiverId, boolean createIfNotExist);
}

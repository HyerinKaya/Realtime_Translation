package translation.spring.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;



    //DTO 클래스의 toEntity() 에서 사용하기 위해서 선언
    @Builder
    public Member(String id, String name){
        this.id = id;
        this.name = name;
    }
}

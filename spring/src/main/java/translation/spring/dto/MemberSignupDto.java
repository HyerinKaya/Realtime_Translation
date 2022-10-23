package translation.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import translation.spring.domain.Member;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor
@Getter
@Setter
public class MemberSignupDto {
    private String id;
    private String name;


    // DTO -> 객체
    public Member toEntity(){
        return Member.builder()
                .id(id)
                .name(name)
                .build();
    }


}
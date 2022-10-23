package translation.spring.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import translation.spring.domain.Member;

@Data
@NoArgsConstructor
public class MemberInfoDto {

    private String id;
    private String name;


    @Builder
    public MemberInfoDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();

    }
}

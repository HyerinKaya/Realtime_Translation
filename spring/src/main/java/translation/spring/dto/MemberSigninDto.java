package translation.spring.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberSigninDto {
    @NotEmpty
    private String id;

}

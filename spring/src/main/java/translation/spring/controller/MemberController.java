package translation.spring.controller;

import org.springframework.validation.BindingResult;
import translation.spring.dto.MemberInfoDto;
import translation.spring.dto.MemberSignupDto;
import translation.spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     * @param memberSignupDto(id,name)
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/api/members/new")
    public ResponseEntity<?> create(@Valid @RequestBody MemberSignupDto memberSignupDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        memberService.save(memberSignupDto);
        return new ResponseEntity<>("회원 등록 성공", HttpStatus.OK);
    }


}
package translation.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import translation.spring.domain.Member;
import translation.spring.dto.MemberSigninDto;
import translation.spring.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;


    @PostMapping("/api/members/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberSigninDto form, BindingResult bindingResult,
                                   HttpServletRequest request, HttpServletResponse response) {

        Optional<Member> loginMember = (Optional<Member>) loginService.login(form.getId());

        //조회된 회원이 없는 경우
        if (loginMember.isEmpty()) {
            bindingResult.reject("loginfail", "등록되지 않은 사용자입니다");
            return new ResponseEntity<>("등록되지 않은 사용자입니다", HttpStatus.BAD_REQUEST);
        }

        //request.getSession()은 세션이 있으면 반환하고
        //없으면 신규 생성해서 반환
        HttpSession session = request.getSession(true);
        //세션에 로그인 회원 정보를 보관
        session.setAttribute(session.getId(), loginMember.get());
        session.setAttribute("memberId", loginMember.get().getId());


        System.out.println(session.getId());

        /*filter 에서 넘겨받은 redirectURL을 적용 시키기 위해서 이렇게 바꾸었다.
        로그인을 하지 않고 /items 로 갔다가 로그인 페이지로 리다이렉트 되었다가
        로그인을 하면 /items 페이지로 이동할 것이다. 반면에 로그인을 했으면 defaultvalue 인 "/"가
        적용되어서 home 으로 돌아갈 것이다.
         */
        return new ResponseEntity<>(loginMember.get(), HttpStatus.OK);
    }


    //HttpSession 을 이용한 로그아웃
    @PostMapping("/members/logout")
    public ResponseEntity<?> logoutV3(HttpServletRequest request) {
        //세션을 없애는 것이 목적이기 때문에 false 옵션을 주고 조회해 온다.
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); //세션 만료
            return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
        }
        return new ResponseEntity<>("세션이 만료되었거나 로그인을 하지 않았습니다.", HttpStatus.UNAUTHORIZED);
    }
}

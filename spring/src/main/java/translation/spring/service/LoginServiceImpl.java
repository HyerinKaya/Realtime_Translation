package translation.spring.service;

import translation.spring.domain.Member;
import translation.spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import translation.spring.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;

    //로그인
    public Optional<Member> login(String loginId){
        Optional<Member> findMember = memberRepository.findById(loginId);

        //검색이 안되면 null 반환
        if(findMember == null){
            return null;
        }
        return findMember;
    }

    //로그아웃
}

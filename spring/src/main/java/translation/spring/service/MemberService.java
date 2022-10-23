package translation.spring.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import translation.spring.domain.Member;
import translation.spring.dto.MemberSignupDto;

import java.util.List;
import java.util.Optional;

@Service
public interface MemberService extends UserDetailsService{

    //회원가입
    Member save(MemberSignupDto memberDto) throws Exception;

    //특정 회원 조회
    Optional<Member> findOne(String memberId) throws Exception;

    //회원 전체 조회
    List<Member> findAll() throws Exception;

}

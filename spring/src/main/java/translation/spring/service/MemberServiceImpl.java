package translation.spring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import translation.spring.domain.Member;
import translation.spring.dto.MemberSignupDto;
import translation.spring.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //final 키워드가 붙은 것을 생성자 의존 관계 주입
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param
     * @return
     */
    @Override
    public Member save(MemberSignupDto memberDto) throws Exception{
        return memberRepository.save(memberDto.toEntity());
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException("아이디가 없습니다"));

        return User.builder().username(member.getId())
                .build();

    }

    /**
     * 회원id 로 조회하기
     * @param memberId
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Member> findOne(String memberId) throws Exception{
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty())
            return Optional.empty();
        return findMember;
    }

    /**
     * 모든 회원 조회하기
     * @return
     * @throws Exception
     */
    @Override
    public List<Member> findAll() throws Exception{
        return memberRepository.findAll();
    }

}

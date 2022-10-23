package translation.spring.service;


import translation.spring.domain.Member;

import java.util.Optional;

public interface LoginService {
    Optional<Member> login(String loginId);
}

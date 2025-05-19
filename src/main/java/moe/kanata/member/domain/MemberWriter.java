package moe.kanata.member.domain;

import lombok.RequiredArgsConstructor;
import moe.kanata.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberRepository repository;

    public Member write(final Member member) {
        return repository.save(member);
    }
}

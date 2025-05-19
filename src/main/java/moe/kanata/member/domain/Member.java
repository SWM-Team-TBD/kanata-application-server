package moe.kanata.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moe.kanata.global.domain.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    @Embedded
    private MemberInfo info;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member (
        final String email,
        final MemberInfo info,
        final MemberRole role
    ) {
        this.email = email;
        this.info = info;
        this.role = role;
    }

    public String getProfile() {
        return info.getProfile();
    }

    public String getName() {
        return info.getName();
    }

    public void updateInfo(final MemberInfo info) {
        this.info = info;
    }
}


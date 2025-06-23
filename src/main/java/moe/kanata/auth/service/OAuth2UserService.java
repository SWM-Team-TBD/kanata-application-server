package moe.kanata.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.kanata.member.domain.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User user = super.loadUser(userRequest);

        final Map<String, Object> oAuth2UserAttributes = user.getAttributes();
        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        final OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        final String email = userInfo.email();
        final String profile = userInfo.profile();
        final String name = userInfo.name();
        final MemberInfo memberInfo = new MemberInfo(profile, name);

        final Member member = updateOrWrite(email, memberInfo);

        return new KanataUser(member, oAuth2UserAttributes, userNameAttributeName);
    }

    private Member updateOrWrite(
        final String email,
        final MemberInfo info
    ) {
        if (memberReader.existsByEmail(email)) {
            final Member member = memberReader.readByEmail(email);
            member.updateInfo(info);
            return member;
        }
        final Member member = Member.builder()
            .email(email)
            .info(info)
            .role(MemberRole.ROLE_USER)
            .build();
        return memberWriter.write(member);
    }
}

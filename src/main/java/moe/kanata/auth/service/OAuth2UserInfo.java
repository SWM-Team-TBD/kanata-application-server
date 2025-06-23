package moe.kanata.auth.service;

import lombok.Builder;
import moe.kanata.global.exception.HttpResponseException;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
    String name,
    String email,
    String profile
) {

    public static OAuth2UserInfo of(
        final String registrationId,
        final Map<String, Object> attributes
    ) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw HttpResponseException.badRequest(
                "Unsupported OAuth2 provider: " + registrationId
            );
        };
    }

    private static OAuth2UserInfo ofGoogle(final Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .profile((String) attributes.get("picture"))
            .build();
    }

    private static OAuth2UserInfo ofKakao(final Map<String, Object> attributes) {
        final Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        final Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
            .name((String) profile.get("nickname"))
            .email((String) account.get("email"))
            .profile((String) profile.get("profile_image_url"))
            .build();
    }
}
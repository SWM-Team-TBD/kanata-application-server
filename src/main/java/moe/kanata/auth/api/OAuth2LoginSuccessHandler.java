package moe.kanata.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moe.kanata.auth.service.AccessTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static moe.kanata.auth.api.AuthorizationRequestRedirectResolver.REDIRECT_PARAM_KEY;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String STATE_PARAM_KEY = "state";
    private static final String ACCESS_TOKEN_PARAM_KEY = "accessToken";
    private final AccessTokenService accessTokenService;

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        final String state = request.getParameter(STATE_PARAM_KEY);
        final Map<String, String> stateParams = parseState(state);
        final String redirect = stateParams.getOrDefault(REDIRECT_PARAM_KEY, "/");

        final String accessToken = accessTokenService.generateAccessToken(authentication);
        final String redirectWithAccessToken = UriComponentsBuilder.fromUriString(redirect)
            .queryParam(ACCESS_TOKEN_PARAM_KEY, accessToken)
            .build()
            .toUriString();

        response.sendRedirect(redirectWithAccessToken);
    }

    private Map<String, String> parseState(final String state) {
        final Map<String, String> params = new HashMap<>();
        UriComponentsBuilder.fromUriString("?" + state).build()
            .getQueryParams()
            .forEach((key, value) -> params.put(key, value.getFirst()));
        return params;
    }
}

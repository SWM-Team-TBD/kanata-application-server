package moe.kanata.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static moe.kanata.auth.api.AuthorizationRequestRedirectResolver.REDIRECT_PARAM_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthDocs {

    @GetMapping("/login/{registrationId}")
    public RedirectView login(
        @PathVariable String registrationId,
        @RequestParam(REDIRECT_PARAM_KEY) String redirect
    ) {
        final String url = String.format(
            "/oauth2/authorization/%s?%s=%s",
            registrationId,
            REDIRECT_PARAM_KEY,
            redirect
        );
        return new RedirectView(url);
    }
}

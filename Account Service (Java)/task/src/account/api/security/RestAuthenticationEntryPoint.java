package account.api.security;

import account.domain.SecurityEvent;
import account.domain.UserAccount;
import account.domain.repositories.SecurityEventRepository;
import account.domain.repositories.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static account.api.security.event.SecurityEventType.LOGIN_FAILED;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final SecurityEventRepository securityEventRepository;
    private final UserAccountRepository userAccountRepository;

    public RestAuthenticationEntryPoint(SecurityEventRepository securityEventRepository, UserAccountRepository userAccountRepository) {
        this.securityEventRepository = securityEventRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        String header = request.getHeader("Authorization");

        String email = "";
        if (header != null && header.startsWith("Basic ")) {
            String[] credentials = extractAndDecodeHeader(header);
            if (credentials.length == 2) {
                email = credentials[0];
            }
        }
        if (header != null && !header.isBlank()) {
            saveSecurityEvent(request, email);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    private String[] extractAndDecodeHeader(String header) throws IOException {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new IllegalStateException("Invalid basic authentication token");
        }

        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }

    private void saveSecurityEvent(HttpServletRequest request, String email) {
        UserAccount userAccount = userAccountRepository.findByEmailEqualsIgnoreCase(email).orElse(null);

        SecurityEvent securityEvent = new SecurityEvent();
        securityEvent.setEventName(LOGIN_FAILED);
        securityEvent.setPath((String) request.getAttribute("jakarta.servlet.error.request_uri"));
        securityEvent.setDate(Date.from(Instant.now()));
        securityEvent.setSubjectAccountId(userAccount != null ? userAccount.getId():0L); //
        securityEvent.setObjectAccountId(userAccount != null ? userAccount.getId():0L); //
        securityEvent.setObject(email);
        securityEventRepository.save(securityEvent);
    }
}

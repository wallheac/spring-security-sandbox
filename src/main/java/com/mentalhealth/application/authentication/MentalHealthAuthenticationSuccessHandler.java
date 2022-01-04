package com.mentalhealth.application.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MentalHealthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Log logger = LogFactory.getLog(this.getClass());

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private String determineTargetUrl(Authentication authentication) {
        Map<String, String> targetUrlMap = new HashMap<>();
        targetUrlMap.put("PROVIDER", "/provider/welcome");
        targetUrlMap.put("CONSUMER", "/consumer/welcome");

        Optional<? extends GrantedAuthority> maybeAuthority = authentication.getAuthorities().stream()
                .filter(auth -> targetUrlMap.containsKey(auth.getAuthority())).findFirst();

        if (maybeAuthority.isPresent()) {
             String url = targetUrlMap.get(maybeAuthority.get().getAuthority());
             return url;
        } else {
            throw new IllegalStateException();
        }
    }
}

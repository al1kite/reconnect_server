package reconnect.server.domain.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import reconnect.server.global.constants.AuthConstants;

import java.io.IOException;

import static reconnect.server.global.constants.AuthConstants.BEARER;
import static reconnect.server.global.constants.CommonConstants.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = "";
            if(request.getHeader(AuthConstants.AUTHORIZATION) != null)
                accessToken = request.getHeader(AuthConstants.AUTHORIZATION).replace(BEARER, "");


            if (jwtTokenProvider.validateJwtTokenExpireTime(accessToken)) {
                this.setAuthentication(accessToken);
            } else {
                String refreshToken = request.getHeader(AuthConstants.REFRESH_TOKEN_STR);

                if (jwtTokenProvider.validateJwtTokenExpireTime(refreshToken)) {
                    String newAccessToken = this.createNewAccessToken(refreshToken);

                    response.setHeader(AuthConstants.ACCESS_TOKEN_STR, accessToken);
                    this.setAuthentication(newAccessToken);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String createNewAccessToken(String refreshToken) {
        String userId = jwtTokenProvider.getUserId(refreshToken);
        String email = jwtTokenProvider.getEmail(refreshToken);

        Claims claims = Jwts.claims().setSubject(userId);
        claims.put(EMAIL, email);

        return jwtTokenProvider.createNewAccessToken(claims);
    }

    public void setAuthentication(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

package goormton.backend.somgil.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import goormton.backend.somgil.global.config.security.jwt.domain.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, ObjectMapper objectMapper){
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenProvider.resolveToken(request);
            if (token!=null&&tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
        catch (SignatureException e){
            setResponse(response,"토큰 서명 오류");
        }catch (MalformedJwtException ex){
            setResponse(response,"지원하지 않는 방식의 토큰");
        }catch (ExpiredJwtException ex){
            setResponse(response,"만료된 토큰");
        }catch (IllegalArgumentException ex){
            setResponse(response,"알 수 없는 토큰");
        }
    }

    private void setResponse(HttpServletResponse response,String msg) throws IOException {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(objectMapper.writeValueAsString(msg));
    }

}

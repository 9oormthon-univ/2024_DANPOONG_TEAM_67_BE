package goormton.backend.somgil.global.config.security.jwt.domain;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private final UserDetailsService userDetailsService;
    @Value("${jwtSecret}")
    private String secret;


    private Key secretKey;
    private final long tokenValidMillisecond = 1000 * 60 * 60; //1시간

    @PostConstruct
    protected void init(){
       log.info("키 생성 암호화 전 키 :{}",secret);
       secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
       log.info("키 생성 암호화 후 키 :{}",secretKey);
    }


    public String createToken(String id, List<String> roles, LocalDateTime localDateTime){
        log.info("토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("roles",roles);
        Date now = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date accessExpiredTime = new Date(now.getTime()+tokenValidMillisecond);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessExpiredTime)
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
        log.info("토큰 생성 완료");
        return accessToken;
    }




    public Authentication getAuthentication(String token){
        //log.info("토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        //log.info("토큰 인증 정보 조회 완료 user:{}",userDetails.getUsername());
        //log.info("토큰 인증 정보 조회 완료 user:{}",userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUsername(String token){
            //log.info("토큰으로 회원 정보 추출");
            String info = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
            log.info("토큰으로 회원 정보 추출 완료 info:{}",info);
            return info;

    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Bearer");
    }

    public boolean validateToken(String token){
        //log.info("토큰 유효성 검증 시작");
        return valid(secretKey,token);
    }

    private boolean valid(Key key, String token){
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }


}

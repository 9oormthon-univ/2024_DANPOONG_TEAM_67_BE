package goormton.backend.somgil.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import goormton.backend.somgil.domain.user.dto.response.LoginResponse;
import goormton.backend.somgil.global.config.security.AuthTokens;
import goormton.backend.somgil.global.config.security.jwt.domain.JwtTokenProvider;
import goormton.backend.somgil.global.config.security.jwt.domain.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserRepository userRepository;
    //private final AuthTokensGenerator authTokensGenerator;
    private final TokenProvider tokenProvider;
    private final ClientKakao clientKakao;

    /*@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUrl;*/

    @Transactional
    public LoginResponse kakaoLogin(String code) {

        // 1. "인가 코드"로 "액세스 토큰" 요청 & 토큰으로 카카오 API 호출
        User user = clientKakao.getUserData(clientKakao.getUserKakaoToken(code));
        //2. 카카오ID로 회원가입 & 로그인 처리
        if(!userRepository.existsByKakaoId(user.getKakaoId())){
            userRepository.save(user);
        }
        User loginedUser = userRepository.findByKakaoId(user.getKakaoId()).get();

        String token = tokenProvider.createToken(loginedUser.getId().toString(),loginedUser.getRole(), LocalDateTime.now());
        return new LoginResponse(loginedUser.getId(),loginedUser.getNickname(),loginedUser.getEmail(),token);
    }

    /*private LoginResponse kakaoUserLogin(HashMap<String, Object> userInfo){

        Long uid= Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        User kakaoUser = userRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoUser == null) {    //회원가입
            kakaoUser = User.builder()
                    .nickname(nickName)
                    .email(kakaoEmail).build();
            userRepository.save(kakaoUser);
        }
        //토큰 생성
        AuthTokens token = authTokensGenerator.generate(uid.toString());
        return new LoginResponse(uid, nickName, kakaoEmail,token);
    }*/

    /*private String getAccessToken(String code, String redirectUri) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }*/


}

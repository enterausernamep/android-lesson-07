package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.ExceptionalResultDto;
import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthEndpoint {
    private final UserDataService userDataService;
    private final BCryptPasswordEncoder encoder;


    // JWT 인증을 위해 사용되는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDataEntity entity) {
        try {
            // 로그인을 시도합니다.
            return ResponseEntity.ok(userDataService.createTokenWith(entity));
        } catch (Exception ex) {
            // 만약 로그인에 실패했다면, 400 Bad Request를 반환합니다.
            return ResponseEntity.badRequest().body(new ExceptionalResultDto(ex.getMessage()));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@ModelAttribute UserDataEntity entity) {
        try {
            // 사용자가 이미 존재하는지 확인합니다.
            if (userDataService.isUserExists(entity.getUserId())) {
                return ResponseEntity.badRequest().body(new ExceptionalResultDto("이미 존재하는 사용자입니다"));
            }
            entity.setPassword(encoder.encode(entity.getPassword()));

            // 사용자를 생성합니다.
            userDataService.createUser(entity);
            System.out.println("userId, Password"+entity.getUserId()+"," + entity.getPassword());

            // 성공적인 응답 또는 추가 정보를 반환합니다.
            return ResponseEntity.ok("사용자가 성공적으로 등록되었습니다");
        } catch (Exception ex) {
            // 예외를 적절하게 처리합니다. 필요에 따라 이 부분을 사용자 정의할 수 있습니다.
            return ResponseEntity.badRequest().body(new ExceptionalResultDto(ex.getMessage()));
        }
    }
}

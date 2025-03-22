package yjj.wetrash.domain.pin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjj.wetrash.domain.pin.dto.PinRequestDTO;
import yjj.wetrash.domain.pin.dto.PinResponseDTO;
import yjj.wetrash.domain.pin.service.PinService;
import yjj.wetrash.global.security.CustomDetails;

import java.util.List;

@RestController
@RequestMapping("/api/pin")
@RequiredArgsConstructor
@Tag(name = "Pin API", description = "Pin API입니다.")
public class PinController {

    private final PinService pinService;

    //사용자로부터 핀 요청받기 - 상태: 승인 전
    @PostMapping("/request")
    public ResponseEntity<?> savePin(@AuthenticationPrincipal CustomDetails customDetails, PinRequestDTO pinRequestDTO){
        String email = customDetails.getName();
        pinService.savePin(email, pinRequestDTO);   //pending
        return ResponseEntity.ok().body("핀 저장 완료");
    }

    //관리자페이지 - 보류 상태 요청들 보내기
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PinResponseDTO>> getPendingPins(){
        List<PinResponseDTO> pins =  pinService.getPendingPins();
        return ResponseEntity.ok(pins);
    }


}

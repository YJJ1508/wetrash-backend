package yjj.wetrash.domain.pin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.pin.dto.PinRequestDTO;
import yjj.wetrash.domain.pin.dto.PinResponseDTO;
import yjj.wetrash.domain.pin.service.PinService;
import yjj.wetrash.global.security.CustomDetails;

import java.util.List;

@RestController
@RequestMapping("/api/pin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pin API", description = "Pin API입니다.")
public class PinController {

    private final PinService pinService;

    //사용자로부터 핀 요청받기 - 상태: 승인 전
    @PostMapping("/request")
    public ResponseEntity<?> savePin(@AuthenticationPrincipal CustomDetails customDetails,
                                     @RequestBody @Valid PinRequestDTO pinRequestDTO){
        String email = customDetails.getName();
        log.info("pin 컨트롤러 pinRequestDTO: {}", pinRequestDTO.getLatitude());
        pinService.savePin(email, pinRequestDTO);   //pending
        return ResponseEntity.ok().body("핀 저장 완료");
    }

    //관리자페이지 - 보류 상태 요청들 보내기
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PinResponseDTO>> getPendingPins(){
        List<PinResponseDTO> pins =  pinService.getPendingPins();
        return ResponseEntity.ok(pins);
    }


}

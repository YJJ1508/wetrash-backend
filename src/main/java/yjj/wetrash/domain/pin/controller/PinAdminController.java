package yjj.wetrash.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.member.dto.AdminMemberReputationResDTO;
import yjj.wetrash.domain.pin.dto.PinApprovalReqDTO;
import yjj.wetrash.domain.pin.dto.PinRejectionReqDTO;
import yjj.wetrash.domain.pin.dto.PinResponseDTO;
import yjj.wetrash.domain.pin.service.PinService;

import java.util.List;

@RestController
@RequestMapping("/api/pin/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class PinAdminController {

    private final PinService pinService;

    //관리자페이지 - 보류 상태 요청들 보내기
    @GetMapping("/pending")
    public ResponseEntity<List<PinResponseDTO>> getPendingPins(){
        List<PinResponseDTO> pins =  pinService.getPendingPins();
        return ResponseEntity.ok(pins);
    }

    @PatchMapping("/approve")
    public ResponseEntity<Void> approvePin(@RequestBody List<PinApprovalReqDTO> dtos){
        pinService.updateApprovalPins(dtos);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reject")
    public ResponseEntity<Void> rejectPin(@RequestBody List<PinRejectionReqDTO> dtos){
        pinService.updateRejectionPins(dtos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reputation")
    public ResponseEntity<List<AdminMemberReputationResDTO>> getMemberReputations(){
        List<AdminMemberReputationResDTO> list = pinService.getMemberReputations();
        return ResponseEntity.ok(list);
    }

}

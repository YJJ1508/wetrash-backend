package yjj.wetrash.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.member.dto.AdminMemberReputationResDTO;
import yjj.wetrash.domain.pin.dto.PinAdminUpdateReqDTO;
import yjj.wetrash.domain.pin.dto.PinApprovalReqDTO;
import yjj.wetrash.domain.pin.dto.PinRejectionReqDTO;
import yjj.wetrash.domain.pin.dto.PinAdminResDTO;
import yjj.wetrash.domain.pin.service.PinAdminService;
import yjj.wetrash.domain.pin.service.PinService;

import java.util.List;

@RestController
@RequestMapping("/api/pin/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class PinAdminController {

    private final PinAdminService pinAdminService;

    //관리자페이지 - 보류/승인 상태 요청들 보내기
    @GetMapping("/pins")
    public ResponseEntity<List<PinAdminResDTO>> getPinsByStatus(@RequestParam String status){
        List<PinAdminResDTO> pins =  pinAdminService.getPinsByStatus(status);
        return ResponseEntity.ok(pins);
    }

    @PatchMapping("/approve")
    public ResponseEntity<Void> approvePin(@RequestBody List<PinApprovalReqDTO> dtos){
        pinAdminService.updateApprovalPins(dtos);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reject")
    public ResponseEntity<Void> rejectPin(@RequestBody List<PinRejectionReqDTO> dtos){
        pinAdminService.updateRejectionPins(dtos);
        return ResponseEntity.ok().build();
    }

    //핀 데이터 수정
    @PatchMapping("/update")
    public ResponseEntity<Void> updateSelectedPin(@RequestBody List<PinAdminUpdateReqDTO> dtos){
        pinAdminService.updateApprovedPinsByAdmin(dtos);
        return ResponseEntity.ok().build();
    }

    //핀 숨기기
    @PatchMapping("/hide")
    public ResponseEntity<Void> updatePinStateToRemoved(@RequestBody List<Long> pinIds){
        pinAdminService.updatePinStateToRemoved(pinIds);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/restore")
    public ResponseEntity<Void> updatePinStateToApproved(@RequestBody List<Long> pinIds){
        pinAdminService.updatePinStateToApproved(pinIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteSelectedPin(@RequestBody List<Long> pinIds){
        pinAdminService.deleteApprovedPins(pinIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reputation")
    public ResponseEntity<List<AdminMemberReputationResDTO>> getMemberReputations(){
        List<AdminMemberReputationResDTO> list = pinAdminService.getMemberReputations();
        return ResponseEntity.ok(list);
    }

}

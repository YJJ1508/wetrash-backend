package yjj.wetrash.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.pin.dto.PinApprovalReqDTO;
import yjj.wetrash.domain.pin.service.PinService;

import java.util.List;

@RestController
@RequestMapping("/api/pin/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class PinAdminController {

    private final PinService pinService;

    @PatchMapping("/approve")
    public ResponseEntity<Void> approvePin(@RequestBody List<PinApprovalReqDTO> dtos){
        pinService.approvePin(dtos);
        return ResponseEntity.ok().build();
    }

}

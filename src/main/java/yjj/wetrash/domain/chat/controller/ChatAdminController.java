package yjj.wetrash.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjj.wetrash.domain.chat.dto.ChatMessageAdminDTO;
import yjj.wetrash.domain.chat.service.ChatAdminService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ChatAdminController {

    private final ChatAdminService chatAdminService;

    @GetMapping("/reports-all")
    public ResponseEntity<List<ChatMessageAdminDTO>> getAllReports(){
        List<ChatMessageAdminDTO> DTOs = chatAdminService.getAllReports();
        return ResponseEntity.ok(DTOs);
    }

    @GetMapping("/reports-flagged")
    public ResponseEntity<List<ChatMessageAdminDTO>> getReportsOverFive(){
        List<ChatMessageAdminDTO> DTOs = chatAdminService.getReportMessagesGreaterThanCount();
        return ResponseEntity.ok(DTOs);
    }

}

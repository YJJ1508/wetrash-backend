package yjj.wetrash.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yjj.wetrash.domain.chat.dto.ChatMessageReportReqDTO;
import yjj.wetrash.domain.chat.service.ChatReportService;
import yjj.wetrash.global.security.CustomDetails;

@RestController
@RequestMapping("/api/chatReport")
@RequiredArgsConstructor
public class ChatReportController {

    private final ChatReportService chatReportService;

    @PostMapping("/save")
    public ResponseEntity<Void> reportMessage(@AuthenticationPrincipal CustomDetails customDetails,
                                              @RequestBody ChatMessageReportReqDTO chatMessageReportDTO){
        String email = customDetails.getName();
        chatReportService.saveReportMessage(email, chatMessageReportDTO);
        return ResponseEntity.ok().build();
    }

}

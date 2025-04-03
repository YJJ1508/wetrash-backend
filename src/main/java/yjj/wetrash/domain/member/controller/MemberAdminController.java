package yjj.wetrash.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.member.dto.MemberWarningReqDTO;
import yjj.wetrash.domain.member.dto.UserListDTO;
import yjj.wetrash.domain.member.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/user/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> getUserList(){
        List<UserListDTO> users = memberService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/admins")
    public ResponseEntity<List<UserListDTO>> getAdminList(){
        List<UserListDTO> users = memberService.getAllAdmins();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/warn")
    public ResponseEntity<Void> addWarning(@RequestBody MemberWarningReqDTO dto){
        memberService.addWarning(dto);
        return ResponseEntity.ok().build();
    }


}

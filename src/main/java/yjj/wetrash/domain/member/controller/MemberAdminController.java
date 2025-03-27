package yjj.wetrash.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}

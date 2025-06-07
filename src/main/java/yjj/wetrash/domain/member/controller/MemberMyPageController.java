package yjj.wetrash.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yjj.wetrash.domain.member.dto.mypage.*;
import yjj.wetrash.domain.member.service.MemberMyPageService;
import yjj.wetrash.global.security.CustomDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/mypage")
public class MemberMyPageController {

    private final MemberMyPageService memberMyPageService;

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResDTO> getMemberProfile(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getName();
        MemberProfileResDTO myProfileInfo = memberMyPageService.getMyProfileInfo(email);
        return ResponseEntity.ok(myProfileInfo);
    }

    @PostMapping("/nickname")
    public ResponseEntity<Void> checkNicknameDuplicate(@RequestBody NicknameCheckReqDTO nicknameCheckReqDTO){
        boolean exists = memberMyPageService.checkNicknameDuplicate(nicknameCheckReqDTO.getNickname());
        if (exists) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/nickname-update")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal CustomDetails customDetails,
                                               @RequestBody NicknameCheckReqDTO nicknameCheckReqDTO){
        String email = customDetails.getName();
        memberMyPageService.updateNickname(email, nicknameCheckReqDTO.getNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile-update")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal CustomDetails customDetails,
                                              @RequestParam(value = "file", required = false) MultipartFile multipartFile){
        String email = customDetails.getName();
        memberMyPageService.updateProfile(email, multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewsResDTO>> getMemberReviews(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getName();
        List<ReviewsResDTO> reviews = memberMyPageService.getMemberReviews(email);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoritesResDTO>> getMemberFavorites(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getName();
        List<FavoritesResDTO> favorites = memberMyPageService.getMemberFavorites(email);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/tier")
    public ResponseEntity<PointResDTO> getMemberTier(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getName();
        PointResDTO tier = memberMyPageService.getMemberPointTier(email);
        return ResponseEntity.ok(tier);
    }

    @GetMapping("/tier-detail")
    public ResponseEntity<List<PointDetailResDTO>> getMemberTierDetail(@AuthenticationPrincipal CustomDetails customDetails){
        String email = customDetails.getName();
        List<PointDetailResDTO> tierDetail = memberMyPageService.getMemberPointDetail(email);
        return ResponseEntity.ok(tierDetail);
    }


}

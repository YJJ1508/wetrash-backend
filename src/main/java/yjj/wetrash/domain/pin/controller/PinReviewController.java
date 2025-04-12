package yjj.wetrash.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.pin.dto.PinReviewReqDTO;
import yjj.wetrash.domain.pin.dto.PinReviewUpdateReqDTO;
import yjj.wetrash.domain.pin.service.PinReviewService;
import yjj.wetrash.global.security.CustomDetails;

@RestController
@RequestMapping("/api/pin/pinReview")
@RequiredArgsConstructor
@Slf4j
public class PinReviewController {

    private final PinReviewService pinReviewService;

    @PostMapping("/save")
    public ResponseEntity<?> savePinReview(@AuthenticationPrincipal CustomDetails customDetails,
                                           @RequestBody PinReviewReqDTO pinReviewReqDTO){
        pinReviewService.savePinReview(customDetails.getName(), pinReviewReqDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{reviewId}")
    public ResponseEntity<?> updatePinReview(@AuthenticationPrincipal CustomDetails customDetails,
                                             @PathVariable("reviewId") Long reviewId,
                                             @RequestBody PinReviewUpdateReqDTO pinReviewUpdateReqDTO){
        pinReviewService.updatePinReview(customDetails.getName(), reviewId, pinReviewUpdateReqDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deletePinReview(@AuthenticationPrincipal CustomDetails customDetails,
                                             @PathVariable("reviewId") Long reviewId){
        pinReviewService.deletePinReview(customDetails.getName(), reviewId);
        return ResponseEntity.ok().build();
    }


}

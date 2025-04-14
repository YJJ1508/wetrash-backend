package yjj.wetrash.domain.pin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.pin.service.PinFavoriteService;
import yjj.wetrash.global.security.CustomDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pin/pinFavorite")
public class PinFavoriteController {

    private final PinFavoriteService pinFavoriteService;

    @PostMapping("/{pinId}")
    public ResponseEntity<?> togglePinFavorite(@AuthenticationPrincipal CustomDetails customDetails,
                                             @PathVariable("pinId") Long pinId){
        pinFavoriteService.togglePinFavorite(customDetails.getName(), pinId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{pinId}")
    public ResponseEntity<Boolean> checkPinFavorite(@AuthenticationPrincipal CustomDetails customDetails,
                                                       @PathVariable("pinId") Long pinId){
        Boolean isFavorite = pinFavoriteService.isPinFavorite(customDetails.getName(), pinId);
        return ResponseEntity.ok(isFavorite);
    }

}

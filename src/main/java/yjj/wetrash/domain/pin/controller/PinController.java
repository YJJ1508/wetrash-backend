package yjj.wetrash.domain.pin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yjj.wetrash.domain.pin.dto.PinDetailResDTO;
import yjj.wetrash.domain.pin.dto.PinResDTO;
import yjj.wetrash.domain.pin.dto.PinRequestDTO;
import yjj.wetrash.domain.pin.dto.PinSearchResDTO;
import yjj.wetrash.domain.pin.service.PinService;
import yjj.wetrash.global.security.CustomDetails;

import java.util.List;

@RestController
@RequestMapping("/api/pin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pin API", description = "Pin API입니다.")
public class PinController {

    private final PinService pinService;

    //사용자로부터 핀 요청받기 - 상태: 승인 전
    @PostMapping("/request")
    public ResponseEntity<?> savePin(@AuthenticationPrincipal CustomDetails customDetails,
                                     @RequestBody @Valid PinRequestDTO pinRequestDTO){
        String email = customDetails.getName();
        pinService.savePin(email, pinRequestDTO);   //pending
        return ResponseEntity.ok().body("핀 저장 완료");
    }

    @GetMapping("/pins") //api/pin/pins/type="regular"
    public ResponseEntity<List<PinResDTO>> getPinsType(@RequestParam("type") String type){
        List<PinResDTO> list = pinService.getPins(type);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{pinId}")  //상세페이지
    public ResponseEntity<PinDetailResDTO> getPinDetail(@PathVariable("pinId") Long pinId){
        PinDetailResDTO pinDetailResDTO = pinService.getDetail(pinId);
        return ResponseEntity.ok(pinDetailResDTO);
    }

    @GetMapping //공유 링크로 핀 id검색시 말풍선 띄우기 위함
    public ResponseEntity<PinResDTO> getSharedPin(@RequestParam("sharedPinId") Long pinId){
        PinResDTO pinResDTO = pinService.getSharedPin(pinId);
        return ResponseEntity.ok(pinResDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PinSearchResDTO>> getSearchPins(@RequestParam("keyword") String keyword,
                                                               @RequestParam(value = "userLat", required = false) Double userLat,
                                                               @RequestParam(value = "userLng", required = false) Double userLng,
                                                               @RequestParam(value = "sortType", required = false) String sortType,
                                                               @RequestParam(value = "trashType", required = false) String trashType,
                                                               Pageable pageable
                                           ){
        keyword = keyword.trim();
        log.info("sortTYpe: {}", sortType);
        log.info("trashType: {}", trashType);
        log.info("pageable: {}", pageable);
        Page<PinSearchResDTO> searchResDTOS = pinService.getSearchPins(keyword, userLat, userLng,
                sortType, trashType, pageable);
        return ResponseEntity.ok(searchResDTOS);
    }


}

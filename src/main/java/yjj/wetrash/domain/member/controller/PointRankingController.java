package yjj.wetrash.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yjj.wetrash.domain.member.dto.PointRankingResDTO;
import yjj.wetrash.domain.member.service.PointHistoryService;

import java.util.List;

@Controller
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointRankingController {

    private final PointHistoryService pointHistoryService;

    @GetMapping("/top10")
    public ResponseEntity<List<PointRankingResDTO>> getTop10Ranking(){
        List<PointRankingResDTO> top10 = pointHistoryService.getTop10Members();
        return ResponseEntity.ok(top10);
    }

    @GetMapping("/last-month-top10")
    public ResponseEntity<List<PointRankingResDTO>> getTop10RankingInLastMonth(){
        List<PointRankingResDTO> top10 = pointHistoryService.getTop10MembersInLastMonth();
        return ResponseEntity.ok(top10);
    }
}

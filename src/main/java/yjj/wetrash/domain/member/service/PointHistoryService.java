package yjj.wetrash.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import yjj.wetrash.domain.member.dto.PointRankingResDTO;
import yjj.wetrash.domain.member.entity.Member;
import yjj.wetrash.domain.member.entity.PointHistory;
import yjj.wetrash.domain.member.repository.MemberRepository;
import yjj.wetrash.domain.member.repository.PointHistoryRepository;
import yjj.wetrash.global.util.DateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final DateUtil dateUtil;

    public List<PointHistory> getAllPoints(Member member){
        return pointHistoryRepository.findAllByMember(member);
    }

    //point 등급 계산
    public String calculateTier(Member member){
        int totalPoint = member.getTotalPoint();
        if (totalPoint >= 4000) return "GRANDMASTER";
        else if (totalPoint >= 3000) return "MASTER";
        else if (totalPoint >= 2000) return "DIAMOND";
        else if (totalPoint >= 1000) return "PLATINUM";
        else if (totalPoint >= 300) return "GOLD";
        else if (totalPoint >= 15) return  "SILVER";
        else return "BRONZE";
    }
//    private boolean isTop10(Member member){
//        List<Long> top10 = memberRepository.findMemberIdTop10ByTotalPoint(PageRequest.of(0,10));
//        return top10.contains(member.getId());
//    }

    //point top 10 조회
    @Transactional
    public List<PointRankingResDTO> getTop10Members(){
        List<Member> top10 = memberRepository.findMemberTop10ByTotalPoint(PageRequest.of(0,10));

        //동점 처리
        List<PointRankingResDTO> result = new ArrayList<>();
        int currentRank = 1;
        int prevPoint = -1;
        for (int i = 0; i < top10.size(); i++){
            Member m = top10.get(i);
            int point = m.getTotalPoint();
            if (point != prevPoint) currentRank = i + 1;
            result.add(PointRankingResDTO.from(m, currentRank));
            prevPoint = point;
        }
        return result;
    }

    //point 지난달 top10 조회
    @Transactional
    public List<PointRankingResDTO> getTop10MembersInLastMonth(){
        LocalDateTime[] range = dateUtil.getLastMonthRange();
        List<Object[]> top10 = pointHistoryRepository.findTopMembersByPointInLastMonth(range[0], range[1]);
        //동점 처리
        List<PointRankingResDTO> result = new ArrayList<>();
        int currentRank = 1;
        int prevPoint = -1;
        for (int i = 0; i < top10.size(); i++){
            Object[] o = top10.get(i);
            int point = ((Number) o[2]).intValue();
            if (point != prevPoint) currentRank = i + 1;
            result.add(PointRankingResDTO.fromRaw(o, currentRank));
            prevPoint = point;
        }
        return result;
    }



}

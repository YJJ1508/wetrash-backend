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

        return IntStream.range(0, top10.size())
                .mapToObj(i -> PointRankingResDTO.from(top10.get(i), i+1))
                .collect(Collectors.toList());
    }

    //point 지난달 top10 조회
    @Transactional
    public List<PointRankingResDTO> getTop10MembersInLastMonth(){
        LocalDateTime[] range = dateUtil.getLastMonthRange();
        log.info("range[0]: {}, range[1]: {}", range[0], range[1]);
        List<Object[]> top10 = pointHistoryRepository.findTopMembersByPointInLastMonth(range[0], range[1]);
        log.info("Top10 size = " + top10.size());
        return IntStream.range(0,10)
                .mapToObj(i -> PointRankingResDTO. fromRaw(top10.get(i), i+1))
                .collect(Collectors.toList());
    }


}

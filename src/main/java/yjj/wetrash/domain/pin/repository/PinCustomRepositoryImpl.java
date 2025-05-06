package yjj.wetrash.domain.pin.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import yjj.wetrash.domain.pin.dto.PinSearchResDTO;
import yjj.wetrash.domain.pin.entity.Pin;

import static yjj.wetrash.domain.pin.entity.QPin.pin;
import static yjj.wetrash.domain.pin.entity.QPinFavorite.pinFavorite;
import static yjj.wetrash.domain.pin.entity.QPinReview.pinReview;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class PinCustomRepositoryImpl implements PinCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PinSearchResDTO> searchPins(String keyword, Double userLat, Double userLng,
                                            String sortType, String trashType, Pageable pageable) {
        //제목 우선 정렬
        var titlePriorityCase = Expressions.numberTemplate(Integer.class,
                "CASE WHEN {0} LIKE CONCAT('%', {1}, '%') THEN 1 ELSE 0 END",
            pin.title, keyword);
        //거리 계산
        NumberTemplate<Double> distance = null;
        if (userLat != null && userLng != null){
            distance = Expressions.numberTemplate(Double.class,
                    "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                    userLat, pin.latitude, pin.longitude, userLng);
        }
        //정렬 조건 분기
        OrderSpecifier<?> sortOrder = null;
        if ("거리순".equals(sortType) && distance != null){
            sortOrder = distance.asc();
        } else if ("즐겨찾기순".equals(sortType)) {
            sortOrder = pinFavorite.count().desc();
        } else if ("리뷰순".equals(sortType)){
            sortOrder = pinReview.countDistinct().desc();
        }

        List<PinSearchResDTO> results = queryFactory
                .select(Projections.constructor(PinSearchResDTO.class,
                        pin.id,
                        pin.title,
                        pin.latitude,
                        pin.longitude,
                        pinReview.countDistinct()
                        ))
                .from(pin)
                .leftJoin(pinFavorite).on(pinFavorite.pin.eq(pin))
                .leftJoin(pin.pinReviews, pinReview)
                .where(searchTitle(keyword)
                        .or(searchDescription(keyword))
                        .and(filterByTypes(trashType))
                )
                .groupBy(pin.id) //즐겨찾기 count pin별로 묶기
                .orderBy(
                        (sortOrder == null ? titlePriorityCase.desc() : sortOrder)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pin.id.countDistinct())
                .from(pin)
                .leftJoin(pinFavorite).on(pinFavorite.pin.eq(pin))
                .leftJoin(pin.pinReviews, pinReview)
                .where(searchTitle(keyword)
                        .or(searchDescription(keyword)))
                .groupBy(pin.id);
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchTitle(String keyword){
        return keyword != null && !keyword.trim().isEmpty()
                ? pin.title.contains(keyword)
                : null;
    }
    private BooleanExpression searchDescription(String keyword){
        return keyword != null && !keyword.trim().isEmpty()
                ? pin.description.contains(keyword)
                : null;
    }

    private BooleanExpression filterByTypes(String typesParam){
        if (typesParam == null || typesParam.isBlank()) return null; //필터 선택 안한 case

        List<String> types = Arrays.asList(typesParam.split(",")); // 일쓰/재쓰/둘 다
        log.info("types: {} ", types);

        BooleanExpression condition = null;
        if (types.contains("일반쓰레기")){
            condition = pin.trashcanType1.eq("일반쓰레기"); //pin.trashcanType1 = 일반쓰레기
        }
        if (types.contains("재활용쓰레기")){
            BooleanExpression type2Condition = pin.trashcanType2.eq("재활용쓰레기");
            condition = ( (condition == null) ? type2Condition : condition.and(type2Condition) );
        }
        log.info("condition: {}", condition);
        return condition;
    }

}

package yjj.wetrash.domain.pin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//관리자가 승인 요청 보냄
@Getter
public class PinApprovalReqDTO {

    private Long id;
    private String title;
    private String description;

}

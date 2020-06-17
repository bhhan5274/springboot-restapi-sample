package com.bhhan.restapi.domain;

import com.bhhan.restapi.service.dto.EventDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by hbh5274@gmail.com on 2020-06-15
 * Github : http://github.com/bhhan5274
 */

@Entity
@Table(name = "EVENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
public class Event {

    public enum EventStatus {
        DRAFT, PUBLISHED, BEGAN_ENROLLMENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @Builder
    public Event(Long id, String name, String description, LocalDateTime beginEnrollmentDateTime, LocalDateTime closeEnrollmentDateTime,
                 LocalDateTime beginEventDateTime, LocalDateTime endEventDateTime, String location, int basePrice, int maxPrice,
                 int limitOfEnrollment, Long accountId){
        this.id = id;
        this.name = name;
        this.description = description;
        this.beginEnrollmentDateTime = beginEnrollmentDateTime;
        this.closeEnrollmentDateTime = closeEnrollmentDateTime;
        this.beginEventDateTime = beginEventDateTime;
        this.endEventDateTime = endEventDateTime;
        this.location = location;
        this.basePrice = basePrice;
        this.maxPrice = maxPrice;
        this.limitOfEnrollment = limitOfEnrollment;
        this.free = isFree(basePrice, maxPrice);
        this.offline = isOffline(location);
        this.accountId = accountId;
    }

    public void updateEvent(EventDto.EventReq eventReq){
        this.name = eventReq.getName();
        this.description = eventReq.getDescription();
        this.beginEnrollmentDateTime = eventReq.getBeginEnrollmentDateTime();
        this.closeEnrollmentDateTime = eventReq.getCloseEnrollmentDateTime();
        this.beginEventDateTime = eventReq.getBeginEventDateTime();
        this.endEventDateTime = eventReq.getEndEventDateTime();
        this.location = eventReq.getLocation();
        this.basePrice = eventReq.getBasePrice();
        this.maxPrice = eventReq.getMaxPrice();
        this.limitOfEnrollment = eventReq.getLimitOfEnrollment();
    }

    private boolean isFree(int basePrice, int maxPrice) {
        return basePrice == 0 && maxPrice == 0;
    }

    private boolean isOffline(String location){
        return !Objects.isNull(location) && !location.isBlank();
    }
}

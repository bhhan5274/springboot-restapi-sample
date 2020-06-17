package com.bhhan.restapi.web;

import com.bhhan.restapi.config.InitConfig;
import com.bhhan.restapi.domain.Event;
import com.bhhan.restapi.domain.EventRepository;
import com.bhhan.restapi.service.AccountService;
import com.bhhan.restapi.service.dto.EventDto;
import com.bhhan.restapi.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hbh5274@gmail.com on 2020-06-15
 * Github : http://github.com/bhhan5274
 */

@Import(InitConfig.class)
class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @DisplayName("이벤트 생성 테스트")
    @Test
    void createEvent() throws Exception {
        final Event event = makeEvent("Spring").build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(new EventDto.EventReq(event))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Enroll time of begin new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("Enroll time of end new event"),
                                fieldWithPath("beginEventDateTime").description("Time of begin new event"),
                                fieldWithPath("endEventDateTime").description("Time of end new event"),
                                fieldWithPath("location").description("Location of event"),
                                fieldWithPath("basePrice").description("BasePrice of event"),
                                fieldWithPath("maxPrice").description("MaxPrice of event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("Id of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("free").description("Free of new event"),
                                fieldWithPath("offline").description("Offline of new event"),
                                fieldWithPath("eventStatus").description("EventStatus of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Enroll time of begin new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("Enroll time of end new event"),
                                fieldWithPath("beginEventDateTime").description("Time of begin new event"),
                                fieldWithPath("endEventDateTime").description("Time of end new event"),
                                fieldWithPath("location").description("Location of event"),
                                fieldWithPath("basePrice").description("BasePrice of event"),
                                fieldWithPath("maxPrice").description("MaxPrice of event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @DisplayName("이벤트 생성 테스트 > BAD REQUEST > EMPTY INPUT")
    @Test
    void createEvent_fail_emptyInput() throws Exception {
        final Event.EventBuilder eventBuilder = makeEvent("Spring");
        final Event event = eventBuilder.name("")
                .description("")
                .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("_links.index.href").exists());
    }

    @DisplayName("이벤트 생성 테스트 > BAD REQUEST > Wrong Period")
    @Test
    void createEvent_fail_period() throws Exception {
        final Event.EventBuilder eventBuilder = makeEvent("Spring");
        final Event event = eventBuilder.beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 20, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 20, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 11, 14, 21))
                .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이벤트 생성 테스트 > BAD REQUEST > Wrong Price")
    @Test
    void createEvent_fail_price() throws Exception {
        final Event.EventBuilder eventBuilder = makeEvent("Spring");
        final Event event = eventBuilder.basePrice(10000)
                .maxPrice(200)
                .build();

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].objectName").exists())
                .andExpect(jsonPath("$.errors[0].field").exists())
                .andExpect(jsonPath("$.errors[0].defaultMessage").exists())
                .andExpect(jsonPath("$.errors[0].code").exists())
                .andExpect(jsonPath("$.errors[0].rejectedValue").exists());
    }

    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    @Test
    void queryEvents1() throws Exception {
        IntStream.rangeClosed(1, 30)
                .forEach(i -> eventRepository.save(makeEvent("event " + i)
                .build()));

        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
                .andExpect(jsonPath("_embedded.eventResourceList[0]._links.profile").exists())
                .andDo(document("query-events"));
    }

    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기 > 인증정보 추가")
    @Test
    void queryEvents2() throws Exception {
        IntStream.rangeClosed(1, 30)
                .forEach(i -> eventRepository.save(makeEvent("event " + i)
                        .build()));

        this.mockMvc.perform(get("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
                .andExpect(jsonPath("_embedded.eventResourceList[0]._links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"));
    }

    @DisplayName("1건 이벤트 조회 > 성공")
    @Test
    void getEvent_success() throws Exception {
        final Event savedEvent = eventRepository.save(makeEvent("event 1").build());

        this.mockMvc.perform(get("/api/events/" + savedEvent.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @DisplayName("1건 이벤트 조회 > 실패")
    @Test
    void getEvent_fail() throws Exception {
        this.mockMvc.perform(get("/api/events/100"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("이벤트 수정 > 성공")
    @Test
    void updateEvent_success() throws Exception {
        final Event savedEvent = eventRepository.save(makeEvent("event 1").build());

        final EventDto.EventReq updateEventReq = new EventDto.EventReq(savedEvent);
        final String updateName = "Updated Event";
        updateEventReq.setName(updateName);

        this.mockMvc.perform(put("/api/events/{id}", savedEvent.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEventReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(updateName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @DisplayName("이벤트 수정 > 실패 > 입력값이 비어있는 경우")
    @Test
    void updateEvent_fail1() throws Exception {
        final Event savedEvent = eventRepository.save(makeEvent("event 1").build());

        final EventDto.EventReq updateEventReq = new EventDto.EventReq(savedEvent);
        final String updateName = "";
        updateEventReq.setName(updateName);

        this.mockMvc.perform(put("/api/events/{id}", savedEvent.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEventReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이벤트 수정 > 실패 > 입력값이 잘못되어 경우")
    @Test
    void updateEvent_fail2() throws Exception {
        final Event savedEvent = eventRepository.save(makeEvent("event 1").build());

        final EventDto.EventReq updateEventReq = new EventDto.EventReq(savedEvent);
        updateEventReq.setBasePrice(1000);
        updateEventReq.setMaxPrice(100);

        this.mockMvc.perform(put("/api/events/{id}", savedEvent.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEventReq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Event.EventBuilder makeEvent(String name) {
        return Event.builder()
                .name(name)
                .description("REST API Development With Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리");
    }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
        final String email = "bhhan@email.com";
        final String password = "bhhan";

        String clientId = "myApp";
        String clientSecret = "pass";

        final MvcResult mvcResult = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", email)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        return jackson2JsonParser.parseMap(responseBody).get("access_token").toString();
    }
}
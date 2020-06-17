package com.bhhan.restapi.web.index;

import com.bhhan.restapi.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hbh5274@gmail.com on 2020-06-16
 * Github : http://github.com/bhhan5274
 */

class IndexControllerTest extends BaseControllerTest {

    @DisplayName("Index 테스트")
    @Test
    void index() throws Exception {
        this.mockMvc.perform(get("/api"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }
}
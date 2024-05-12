package com.test.studyroomreservationsystem.global.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    ObjectMapper objectMapper = new ObjectMapper();

    private static String KEY_LOGINID = "username";
    private static String KEY_PASSWORD = "password";
    private static String LOGINID = "username";
    private static String PASSWORD = "123456789";
    private static String LOGIN_URL = "/login";



    private void clear(){
        em.flush();
        em.clear();
    }
    @BeforeEach
    public void init(){
        userRepository.save(User.builder()
                .username(LOGINID)
                .password(bCryptPasswordEncoder.encode(PASSWORD))
                .name("테스트")
                .serial("00000000")
                .build());
        log.info("\n로그인아이디 저장 loginId: {}", userRepository.findByUsername(LOGINID).get().getUsername());
        log.info("\n로그인비번 저장 Password: {}", userRepository.findByUsername(LOGINID).get().getPassword());
        clear();
    }
    private Map getUsernamePasswordMap(String username, String password){
        Map<String, String> map = new HashMap<>();
        map.put(KEY_LOGINID, username);
        map.put(KEY_PASSWORD, password);
        log.info("\ngetLoginIdPasswordMap 메서드 호출 : {}",map);
        return map;
    }

    private ResultActions perform(String url, MediaType mediaType, Map usernamePasswordMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(usernamePasswordMap)));

    }

    @Test
    public void 로그인_성공() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD);


        //when, then
        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }
    @Test
    public void 로그인_실패_아이디틀림_UNAUTHORIZED_401() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID+"1", PASSWORD);

        //when, then
        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
    @Test
    public void 로그인_실패_비밀번호틀림_UNAUTHORIZED_401() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD+"1");

        //when, then
        MvcResult result = perform(LOGIN_URL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

    }
    @Test
    public void 로그인_URL이_틀리면_FORBIDDEN_403() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD);

        //when, then
        perform(LOGIN_URL+"1", APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    public void 로그인_데이터형식_JSON이_아니면_415_Unsupported_Media_Type() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD);

        //when, then
        perform(LOGIN_URL, TEXT_PLAIN, map)
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andReturn();
    }

    @Test
    public void 로그인_HTTP_METHOD_GET이면_405_NOTFOUND() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD);


        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .get(LOGIN_URL)
                        .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                        .content(objectMapper.writeValueAsString(map)))
                .andDo(print())
//                .andExpect(status().isNotFound());
                .andExpect(status().isMethodNotAllowed());
    }



    @Test
    public void 오류_로그인_HTTP_METHOD_PUT이면_405_NOTFOUND() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(LOGINID, PASSWORD);


        //when
        mockMvc.perform(MockMvcRequestBuilders
                        .put(LOGIN_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map)))
                .andDo(print())
//                .andExpect(status().isNotFound());
                .andExpect(status().isMethodNotAllowed());
    }
}

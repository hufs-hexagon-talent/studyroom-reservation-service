package hufs.computer.studyroom.domain.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//권한 부족 처리
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn("[AUTH_WARNING] 권한이 없는 경로 {} 에 대한 요청 {}", request.getRequestURI(), accessDeniedException.getMessage());
        ResponseEntity<Object> errorResponse = ResponseFactory.failure(AuthErrorCode.ACCESS_DENIED);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorResponse.getStatusCode().value());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
        response.getWriter().flush();
    }
}



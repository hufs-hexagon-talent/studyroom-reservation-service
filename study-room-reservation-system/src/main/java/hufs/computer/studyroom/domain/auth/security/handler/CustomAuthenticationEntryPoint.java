package hufs.computer.studyroom.domain.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.code.ErrorCode;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Object errorObj = request.getAttribute("error");

        ErrorCode errorCode = errorObj instanceof ErrorCode ? (ErrorCode) errorObj : AuthErrorCode.AUTHENTICATION_FAILED;

        ResponseEntity<Object> errorResponse = ResponseFactory.failure(errorCode);
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(errorResponse.getStatusCode().value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse.getBody()));
        response.getWriter().flush();
    }
}

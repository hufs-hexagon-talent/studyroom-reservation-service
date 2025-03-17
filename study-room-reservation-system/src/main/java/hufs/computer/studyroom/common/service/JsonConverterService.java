package hufs.computer.studyroom.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hufs.computer.studyroom.common.error.code.CommonErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsonConverterService {
    private final ObjectMapper objectMapper;

    /**
     * 직렬화
     */
    public <T> String serializeAuthInfo(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.SERIALIZATION_FAILED);
        }
    }

    /**
     * 역직렬화
     */
    public <T> T deserializeAuthInfo(String json, Class<T> classT) {
        try {
            return objectMapper.readValue(json, classT);
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorCode.DESERIALIZATION_FAILED);
        }
    }
}

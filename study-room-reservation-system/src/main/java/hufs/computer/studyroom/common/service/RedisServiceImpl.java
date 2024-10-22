package hufs.computer.studyroom.common.service;

import hufs.computer.studyroom.common.error.code.RedisErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
            /**
             * Redis 값을 등록/수정
             * @param {String} key : redis key
             * @param {String} value : redis value
             * @return {void}
             */
    @Override
    public void setValues(String key, String value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

            /**
             * Redis 값을 등록/수정합니다.
             *
             * @param {String}   key : redis key
             * @param {String}   value: redis value
             * @param {Duration} duration: redis 값 메모리 상의 유효시간.
             * @return {void}
             */
    @Override
    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value,duration);
    }

    @Override
    public String getValue(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object value = valueOperations.get(key);

        if (value == null) {
                throw new CustomException(RedisErrorCode.REDIS_KEY_NOT_FOUND);
        }
        return String.valueOf(value);
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}

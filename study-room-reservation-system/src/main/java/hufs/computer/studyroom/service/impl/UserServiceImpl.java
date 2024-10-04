package hufs.computer.studyroom.service.impl;

import hufs.computer.studyroom.domain.entity.User;
import hufs.computer.studyroom.domain.entity.User.ServiceRole;
import hufs.computer.studyroom.domain.repository.UserRepository;
import hufs.computer.studyroom.dto.user.UserInfoUpdateRequestDto;
import hufs.computer.studyroom.dto.user.editpassword.UserPasswordInfoResetRequestDto;
import hufs.computer.studyroom.dto.user.SingUpRequestDto;
import hufs.computer.studyroom.dto.user.editpassword.UserPasswordInfoUpdateRequestDto;
import hufs.computer.studyroom.exception.invalidvalue.InvalidNewPasswordException;
import hufs.computer.studyroom.exception.invalidvalue.InvalidCurrentPasswordException;
import hufs.computer.studyroom.exception.user.EmailAlreadyExistsException;
import hufs.computer.studyroom.exception.user.SerialAlreadyExistsException;
import hufs.computer.studyroom.exception.user.SignUpNotPossibleException;
import hufs.computer.studyroom.service.UserService;
import hufs.computer.studyroom.exception.user.UsernameAlreadyExistsException;
import hufs.computer.studyroom.exception.notfound.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }
    /*-------------------------------------------------*/
    @Override
    public User signUpProcess(SingUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String serial = requestDto.getSerial();
        String email = requestDto.getEmail();

        // 해당 로그인 ID 가 이미 존재하는 아이디인지?
        boolean existsByUsername = userRepository.existsByUsername(username);
        if (existsByUsername) {
            throw (new UsernameAlreadyExistsException(username));
        }
        // 해당 학번이 이미 존재 하는지?
        boolean existsBySerial = userRepository.existsBySerial(serial);
        if (existsBySerial) {
            throw (new SerialAlreadyExistsException(serial));
        }
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            throw (new EmailAlreadyExistsException(email));
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setSerial(serial);
        user.setName(requestDto.getName());
        user.setServiceRole(ServiceRole.USER);
        user.setEmail(email);
        return userRepository.save(user);
    }
    /*-------------------------------------------------*/
    @Override
    @Transactional(rollbackFor = SignUpNotPossibleException.class)
    public List<User> signUpUsers(List<SingUpRequestDto> requestDtos) {
        List<User> userList= new ArrayList<>();

        for (SingUpRequestDto requestDto : requestDtos) {
            User user = signUpProcess(requestDto);
//            todo : 내 생각엔 이부분 안티 패턴 , 프로세스마다 저장하다가 예외터지면 롤백하는 건 문제가있음, 하나하나 말고 리스트 통으로 하는 방법,,?
            userList.add(user);
        }
        return userList;
    }


    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
    @Override
    public User findBySerial(String serial) {
        return userRepository.findBySerial(serial)
                .orElseThrow(() -> new UserNotFoundException(serial));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserInfo(Long userId, UserInfoUpdateRequestDto requestDto) {
        User user = findUserById(userId);
        requestDto.toEntity(user);
        return userRepository.save(user);
    }


    @Override
    public User resetUserPassword(Long userId, UserPasswordInfoUpdateRequestDto userInfoUpdateRequestDto) {
        User user = findUserById(userId);
        String currentPassword = userInfoUpdateRequestDto.getPrePassword();

        // 기존 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException();
        }
        // 새 비밀번호 암호화 및 업데이트
        String newPassword = userInfoUpdateRequestDto.getNewPassword();
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidNewPasswordException();
        }
        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);

        return userRepository.save(user);
    }

    @Override
    public User resetUserPassword(Long userId, UserPasswordInfoResetRequestDto resetRequestDto) {
        User user = findUserById(userId);
        // 새 비밀번호 암호화 및 업데이트
        String newPassword = resetRequestDto.getNewPassword();
        if (bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new InvalidNewPasswordException();
        }
        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        findUserById(userId); //찾아보고 없으면 예외처리
        userRepository.deleteById(userId);
    }

    
}

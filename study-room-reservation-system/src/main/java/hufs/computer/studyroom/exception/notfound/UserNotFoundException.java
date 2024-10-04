package hufs.computer.studyroom.exception.notfound;

public class UserNotFoundException extends RuntimeException implements NotFoundException{
    public UserNotFoundException(Long userId) {
        super("id가 " + userId + "인 User를 찾을 수 없습니다.");
    }
    public UserNotFoundException(String username) {super("username: " + username + "인 User를 찾을 수 없습니다.");}
    public UserNotFoundException() {
        super("User를 찾을 수 없습니다.");
    }
}

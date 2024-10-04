package hufs.computer.studyroom.common.error.exception.notfound;

public class PolicyNotFoundException extends RuntimeException implements NotFoundException{
    public PolicyNotFoundException(Long policyId) {
        super("id가 " + policyId + "인 RoomOperationPolicy를 찾을 수 없습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

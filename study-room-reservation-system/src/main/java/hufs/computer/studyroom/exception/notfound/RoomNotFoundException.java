package hufs.computer.studyroom.exception.notfound;

public class RoomNotFoundException extends RuntimeException implements NotFoundException{
    public RoomNotFoundException(Long roomId) {
        super("id가 " + roomId + "인 Room을 찾을 수 없습니다.");
    }
    public RoomNotFoundException(String roomName) {super("name이 " + roomName + "인 Room을 찾을 수 없습니다.");}
}

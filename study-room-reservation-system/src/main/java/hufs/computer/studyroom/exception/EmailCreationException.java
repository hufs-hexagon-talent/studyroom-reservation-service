package hufs.computer.studyroom.exception;

import jakarta.mail.MessagingException;

public class EmailCreationException extends RuntimeException {
    public EmailCreationException(MessagingException e) {
        super(e);
    }
}

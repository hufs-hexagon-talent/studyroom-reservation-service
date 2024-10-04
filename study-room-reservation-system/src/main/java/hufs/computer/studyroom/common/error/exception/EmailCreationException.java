package hufs.computer.studyroom.common.error.exception;

import jakarta.mail.MessagingException;

public class EmailCreationException extends RuntimeException {
    public EmailCreationException(MessagingException e) {
        super(e);
    }
}

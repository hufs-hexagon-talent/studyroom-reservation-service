package hufs.computer.studyroom.domain.user.repository.projection;

public interface ServiceRoleStats {
    Long getTotalCount();
    Long getUserCount();
    Long getExpiredCount();
    Long getAdminCount();
    Long getBlockedCount();
    Long getResidentCount();
}

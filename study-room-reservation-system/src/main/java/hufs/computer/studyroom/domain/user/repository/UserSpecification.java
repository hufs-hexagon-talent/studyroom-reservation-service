package hufs.computer.studyroom.domain.user.repository;

import hufs.computer.studyroom.domain.user.dto.request.UserSearchCondition;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {
    public static Specification<User> search(UserSearchCondition cond) {
        return Specification.where(username(cond.username()))
                .and(serial(cond.serial()))
                .and(name(cond.name()))
                .and(email(cond.email()))
                .and(role(cond.role()))
                .and(department(cond.departmentId()));
    }

    private static Specification<User> username(String v) {
        return like("username", v);
    }
    private static Specification<User> serial(String v)   { return like("serial", v); }
    private static Specification<User> name(String v)     { return like("name", v); }
    private static Specification<User> email(String v)    { return like("email", v); }


    private static Specification<User> role(ServiceRole r) {
        return (r == null) ? null
                : (root, q, cb) -> cb.equal(root.get("serviceRole"), r);
    }

    private static Specification<User> department(Long id) {
        return (id == null) ? null
                : (root, q, cb) -> cb.equal(root.get("department").get("departmentId"), id);
    }

    /** 부분 일치 Like */
    private static Specification<User> like(String field, String v) {
        return (v == null || v.isBlank()) ? null
                : (root, q, cb) -> cb.like(cb.lower(root.get(field)), "%" + v.toLowerCase() + "%");
    }
}

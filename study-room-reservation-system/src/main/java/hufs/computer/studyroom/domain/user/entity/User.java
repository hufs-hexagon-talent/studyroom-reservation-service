package hufs.computer.studyroom.domain.user.entity;


import hufs.computer.studyroom.domain.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true)
    private String username; //loginId

    @Column(name = "password")
    private String password;

    @Column(name="serial", unique = true)
    private String serial;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="name")
    private String name; // 진짜 이름

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER', 'ADMIN', 'RESIDENT', 'BLOCKED') NOT NULL DEFAULT 'USER'")
    private ServiceRole serviceRole; // 기본값 USER

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}

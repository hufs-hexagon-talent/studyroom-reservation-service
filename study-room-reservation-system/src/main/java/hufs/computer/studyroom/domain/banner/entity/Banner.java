package hufs.computer.studyroom.domain.banner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)  // AuditingEntityListener 등록
@Table(name="banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long bannerId;

    // 배너 유형
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('AD', 'NOTICE')", nullable = false)
    private BannerType bannerType;

    // 배너 이미지 URL
    @Column(name = "image_url")
    private String imageUrl;

    // 클릭 시 이동할 링크 URL
    @Column(name = "link_url")
    private String linkUrl;

    // 배너 활성화 여부
    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    // 엔티티가 처음 생성될 때 자동으로 설정되는 필드
    @CreatedDate
    private Instant createAt;

    // 엔티티가 생성되거나 수정될 때 자동으로 설정되는 필드
    @LastModifiedDate
    private Instant updateAt;
}


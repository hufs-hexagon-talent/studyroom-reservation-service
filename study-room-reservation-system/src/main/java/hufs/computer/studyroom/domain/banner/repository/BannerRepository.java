package hufs.computer.studyroom.domain.banner.repository;

import hufs.computer.studyroom.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByActiveIsTrue();
}

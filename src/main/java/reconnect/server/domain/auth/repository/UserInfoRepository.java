package reconnect.server.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reconnect.server.global.model.entity.mysql.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findUserByEmail(String email);
}

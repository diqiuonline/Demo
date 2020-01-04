package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: 李锦卓
 * Time: 2018/9/9 19:20
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    //根据用户名查找用户
    User findByUsername(String username);
}
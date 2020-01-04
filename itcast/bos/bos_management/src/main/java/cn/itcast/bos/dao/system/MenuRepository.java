package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
}

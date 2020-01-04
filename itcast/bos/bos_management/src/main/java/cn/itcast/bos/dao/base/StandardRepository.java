package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StandardRepository extends JpaRepository<Standard,Integer> {
	@Query(value = "update Standard set minLength = ?2 where id = ?1")
	@Modifying
	public void updateMinLength(Integer id,Integer minLength);
}

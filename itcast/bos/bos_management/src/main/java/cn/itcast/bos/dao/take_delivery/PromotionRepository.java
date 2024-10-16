package cn.itcast.bos.dao.take_delivery;


import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface PromotionRepository extends JpaRepository<Promotion,Integer> {
    @Query( "update Promotion set status='2' where endDate<?1 and status='1'")
    @Modifying
    void updateStatus(Date now);
}

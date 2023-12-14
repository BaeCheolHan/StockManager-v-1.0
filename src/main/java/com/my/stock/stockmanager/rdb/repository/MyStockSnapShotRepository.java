package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.MyStockSnapShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyStockSnapShotRepository extends JpaRepository<MyStockSnapShot, Long> {
}

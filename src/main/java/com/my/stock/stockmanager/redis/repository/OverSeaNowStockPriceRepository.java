package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverSeaNowStockPriceRepository extends CrudRepository<OverSeaNowStockPrice, String> {
}

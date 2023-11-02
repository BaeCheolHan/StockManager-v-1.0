package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrIndexChartRepository extends CrudRepository<KrIndexChart, String> {
}

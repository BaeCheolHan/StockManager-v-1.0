package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.OverSeaIndexChart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverSeaIndexChartRepository extends CrudRepository<OverSeaIndexChart, String> {
}

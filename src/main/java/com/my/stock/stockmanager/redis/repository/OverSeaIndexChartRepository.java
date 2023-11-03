package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.OverSeaIndexChart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OverSeaIndexChartRepository extends CrudRepository<OverSeaIndexChart, String> {
  Optional<OverSeaIndexChart> findByCodeAndChartType(String id, String chartType);
}

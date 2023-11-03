package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KrIndexChartRepository extends CrudRepository<KrIndexChart, String> {
  Optional<KrIndexChart> findByCodeAndChartType(String id, String chartType);
}

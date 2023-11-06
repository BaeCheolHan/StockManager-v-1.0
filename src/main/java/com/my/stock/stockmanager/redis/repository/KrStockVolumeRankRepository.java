package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KrStockVolumeRankRepository extends CrudRepository<KrStockVolumeRank, String> {
}

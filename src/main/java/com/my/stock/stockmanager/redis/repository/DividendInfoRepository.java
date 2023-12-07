package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.DividendInfo;
import org.springframework.data.repository.CrudRepository;

public interface DividendInfoRepository extends CrudRepository<DividendInfo, String> {
}

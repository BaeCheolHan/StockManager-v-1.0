package com.my.stock.stockmanager.redis.repository;

import com.my.stock.stockmanager.redis.entity.RestKisToken;
import org.springframework.data.repository.CrudRepository;

public interface RestKisTokenRepository extends CrudRepository<RestKisToken, String> {
}

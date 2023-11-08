package com.my.stock.stockmanager.mongodb.repository;

import com.my.stock.stockmanager.mongodb.documents.MyStockList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyStockListRepository extends MongoRepository<MyStockList, String> {
}

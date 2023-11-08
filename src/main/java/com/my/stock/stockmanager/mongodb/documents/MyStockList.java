package com.my.stock.stockmanager.mongodb.documents;

import com.my.stock.stockmanager.dto.stock.DashboardStock;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MyStockList {

	@Id
	private String id;

	private List<DashboardStock> data;
}

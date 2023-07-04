package com.my.stock.stockmanager.rdb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
	@Id
	private Long id;
	private String code;
	private String currencyCode;
	private String currencyName;
	private String country;
	private String name;
	private String date;
	private String time;
	private int recurrenceCount;
	private Double basePrice;
	private Double openingPrice;
	private Double highPrice;
	private Double lowPrice;
	@Column(name="\"change\"")
	private String change;
	private Double changePrice;
	private Double cashBuyingPrice;
	private Double cashSellingPrice;
	private Double ttBuyingPrice;
	private Double ttSellingPrice;
	private Double tcBuyingPrice;
	private Double fcSellingPrice;
	private Double exchangeCommission;
	private Double usDollarRate;
	private Double high52wPrice;
	private String high52wDate;
	private Double low52wPrice;
	private String low52wDate;
	private int currencyUnit;
	private String provider;
	private long timestamp;
	private String createdAt;
	private String modifiedAt;
	private Double signedChangePrice;
	private Double signedChangeRate;
	private Double changeRate;

}

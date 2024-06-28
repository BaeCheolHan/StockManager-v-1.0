package com.my.stock.stockmanager.rdb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String currencyCode;
	private String currencyName;
	private String country;
	private String name;
	private String date;
	private String time;
	private int recurrenceCount;
	private BigDecimal basePrice;
	private BigDecimal openingPrice;
	private BigDecimal highPrice;
	private BigDecimal lowPrice;
	@Column(name="\"change\"")
	private String change;
	private BigDecimal changePrice;
	private BigDecimal cashBuyingPrice;
	private BigDecimal cashSellingPrice;
	private BigDecimal ttBuyingPrice;
	private BigDecimal ttSellingPrice;
	private BigDecimal tcBuyingPrice;
	private BigDecimal fcSellingPrice;
	private BigDecimal exchangeCommission;
	private BigDecimal usDollarRate;
	private BigDecimal high52wPrice;
	private String high52wDate;
	private BigDecimal low52wPrice;
	private String low52wDate;
	private int currencyUnit;
	private String provider;
	private long timestamp;
	private String createdAt;
	private String modifiedAt;
	private BigDecimal signedChangePrice;
	private BigDecimal signedChangeRate;
	private BigDecimal changeRate;

}

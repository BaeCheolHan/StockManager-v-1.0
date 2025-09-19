package com.my.stock.stockmanager.rdb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Immutable
@Subselect("""
    SELECT
      d.member_id           AS member_id,
      d.symbol              AS symbol,
      SUM(d.dividend)       AS total_dividend
    FROM dividend d
    GROUP BY d.member_id, d.symbol
""")
@Synchronize("dividend")                 // dividend 변경 시 캐시 동기화
@IdClass(DividendSubSelect.DividendSubSelectId.class)      // ← 복합키로 안정 식별자 제공
public class DividendSubSelect {

	@Id
	@Column(name = "member_id")
	private String memberId;

	@Id
	@Column(name = "symbol")
	private String symbol;

	@Column(name = "total_dividend")
	private BigDecimal totalDividend;

	// 필요하면 rownum을 '표시/정렬용' 컬럼로만 추가 (식별자 X)
	// @Transient 또는 Subselect SQL에 별칭으로 추가 후 @Column 매핑
	// private Long rownum;



	/**
	 * DividendSubSelect 복합키 클래스
	 * 엔티티의 memberId, symbol과 매핑됨
	 */
	public static class DividendSubSelectId implements Serializable {

		private String memberId;
		private String symbol;

		// 기본 생성자 필수
		public DividendSubSelectId() {}

		public DividendSubSelectId(String memberId, String symbol) {
			this.memberId = memberId;
			this.symbol = symbol;
		}

		// getter/setter
		public String getMemberId() {
			return memberId;
		}
		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}

		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		// equals/hashCode 구현 (JPA는 식별자 비교를 여기서 함)
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof DividendSubSelectId)) return false;
			DividendSubSelectId that = (DividendSubSelectId) o;
			return Objects.equals(memberId, that.memberId) &&
					Objects.equals(symbol, that.symbol);
		}

		@Override
		public int hashCode() {
			return Objects.hash(memberId, symbol);
		}
	}
}
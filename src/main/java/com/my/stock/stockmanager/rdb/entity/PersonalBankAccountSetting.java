package com.my.stock.stockmanager.rdb.entity;

import com.my.stock.stockmanager.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalBankAccountSetting extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private BankAccount bankAccount;
	private String defaultNational;
	private String defaultCode;
}

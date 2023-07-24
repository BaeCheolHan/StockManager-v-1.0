package com.my.stock.stockmanager.rdb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
	@OneToOne
	private Member member;

	@JsonBackReference
	@OneToMany(mappedBy = "personalSetting")
	private List<PersonalDetailSetting> personalDetailSetting;

	private Long defaultBankAccountId;

}

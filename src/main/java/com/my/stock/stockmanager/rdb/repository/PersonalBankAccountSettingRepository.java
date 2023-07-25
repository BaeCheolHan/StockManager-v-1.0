package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.PersonalBankAccountSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalBankAccountSettingRepository extends JpaRepository<PersonalBankAccountSetting, Long> {
	Optional<PersonalBankAccountSetting> findByBankAccountId(Long bankAccountId);
}

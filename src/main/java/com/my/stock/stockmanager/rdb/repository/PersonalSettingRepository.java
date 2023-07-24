package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.PersonalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalSettingRepository extends JpaRepository<PersonalSetting, Long> {
	Optional<PersonalSetting> findByDefaultBankAccountId(Long id);
}

package com.my.stock.stockmanager.rdb.entity;

import com.my.stock.stockmanager.base.response.entity.BaseTimeEntity;
import com.my.stock.stockmanager.constants.SnsType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    private String email;

    private String nickName;

    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<BankAccount> bankAccount;

}

package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.stream.Stream;
@Repository
public interface VerificationTokenEntityRepository extends JpaRepository<VerificationTokenEntity, Long> {
    VerificationTokenEntity findByToken(String token);

    VerificationTokenEntity findByUser(UserEntity userEntity);

    Stream< VerificationTokenEntity> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from  VerificationTokenEntity t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}

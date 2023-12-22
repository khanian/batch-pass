package com.example.pass.repository.packaze;

import lombok.experimental.PackagePrivate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE PackageEntity p " +
            "           SET p.count = :count," +
            "               p.period = :period" +
            "       WHERE p.packageSeq = :packageSeq")
//    int updateCountAndPeriod(@Param("packageSeq") Integer packageSeq, @Param("count") Integer count, @Param("period") Integer period);
// "Preferences(Command + ,) > Build, Execution, Deployment > Compiler > Java Compiler" 에서 Additional command line parameters 부분에 -parameters 추가
    int updateCountAndPeriod(Integer packageSeq, Integer count, Integer period);
}

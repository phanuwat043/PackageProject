package com.catelecom.billing.repository;

import com.catelecom.billing.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer>, JpaSpecificationExecutor<Package> {

    //filter with query transaction

    @Transactional
    @Query("select pk from Package pk where pk.package_id = :id")
    List<Package> filterPackageById(@Param("id") Integer id);

    @Transactional
    @Query("select pk from Package pk where pk.package_name like %:name%")
    List<Package> filterPackageByName(@Param("name") String name);

    @Transactional
    @Query("select pk from Package pk where pk.amount = :amount")
    List<Package> filterPackageByAmount(@Param("amount") Integer amount);

    @Transactional
    @Query("select pk from Package pk where pk.create_date = :dateData")
    List<Package> filterPackageByDate(@Param("dateData") String date);

    @Transactional
    @Query("select pk from Package pk where pk.package_id = :id and pk.package_name like %:name% and pk.amount = :amount ")
    List<Package> filterPackageAll(@Param("id") Integer id,
                                   @Param("name") String name,
                                   @Param("amount") Integer amount);
}

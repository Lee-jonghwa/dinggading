package com.mickey.dinggading.domain.band.repository;

import com.mickey.dinggading.domain.band.model.entity.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandRepository extends JpaRepository<Band, Long> {

}
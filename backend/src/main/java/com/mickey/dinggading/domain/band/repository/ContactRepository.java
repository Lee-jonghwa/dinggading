package com.mickey.dinggading.domain.band.repository;

import com.mickey.dinggading.domain.band.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    void deleteAllByBand_BandId(Long bandBandId);

    Optional<Contact> findByContactIdAndBand_BandId(Long contactId, Long bandId);

    List<Contact> findAllByBand_BandId(Long bandId);
}
package com.example.pharmacyproject.pharmacy.service;

import com.example.pharmacyproject.pharmacy.entity.Pharmacy;
import com.example.pharmacyproject.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRepositoryService {

    private final PharmacyRepository pharmacyRepository;

    public void bar(List<Pharmacy>pharmacies) {
        log.info("bar CurrentTransactionName : " + TransactionSynchronizationManager.getCurrentTransactionName());
        foo(pharmacies);
    }

    @Transactional
    public void foo(List<Pharmacy> pharmacies) {
        log.info("foo CurrentTransactionName : " + TransactionSynchronizationManager.getCurrentTransactionName());
        pharmacies.forEach(pharmacy -> {
            pharmacyRepository.save(pharmacy);
            throw new RuntimeException("error");
        });
    }

    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        pharmacyRepository.findById(id).ifPresent(pharmacy -> pharmacy.changePharmacyAddress("서울 특별시 광진구"));
    }

    @Transactional
    public void updateAddress(Long id, String address) {
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElse(null);

        if (Objects.isNull(pharmacy)) {
            log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }

        pharmacy.changePharmacyAddress(address);
    }

    public void updateAddressWithoutTransaction(Long id, String address) {
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElse(null);

        if (Objects.isNull(pharmacy)) {
            log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }

        pharmacy.changePharmacyAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }
}

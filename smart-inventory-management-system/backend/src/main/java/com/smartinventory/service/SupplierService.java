package com.smartinventory.service;

import com.smartinventory.entity.Supplier;
import com.smartinventory.exception.ResourceNotFoundException;
import com.smartinventory.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Supplier> searchSuppliers(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        Supplier existing = getSupplierById(id);
        existing.setName(updatedSupplier.getName());
        existing.setContactPerson(updatedSupplier.getContactPerson());
        existing.setPhone(updatedSupplier.getPhone());
        existing.setEmail(updatedSupplier.getEmail());
        existing.setAddress(updatedSupplier.getAddress());
        return supplierRepository.save(existing);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplierRepository.delete(supplier);
    }
}

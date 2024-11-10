package org.example.flowerswebsite.services;

import org.example.flowerswebsite.DTO.StorageDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StorageService {
    ResponseEntity<StorageDto> createProduct(StorageDto storageDto);
    ResponseEntity<StorageDto> update(StorageDto storageDto);
    ResponseEntity<StorageDto> findById(Long id);
    ResponseEntity<List<StorageDto>> findAll();

}

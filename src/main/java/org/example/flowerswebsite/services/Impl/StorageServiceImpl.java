package org.example.flowerswebsite.services.Impl;

import jakarta.transaction.Transactional;
import org.example.flowerswebsite.DTO.ProductDto;
import org.example.flowerswebsite.DTO.StorageDto;
import org.example.flowerswebsite.Entities.CategoryEntity;
import org.example.flowerswebsite.Entities.ProductEntity;
import org.example.flowerswebsite.Entities.StorageEntity;
import org.example.flowerswebsite.Exceptions.EntityNotFoundException;
import org.example.flowerswebsite.Repositories.StorageRepository;
import org.example.flowerswebsite.services.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final ModelMapper modelMapper;

    public StorageServiceImpl(ModelMapper modelMapper, StorageRepository storageRepository) {
        this.modelMapper = modelMapper;
        this.storageRepository = storageRepository;
    }

    @Override
    public ResponseEntity<StorageDto> createProduct(StorageDto storageDto) {
        StorageEntity storageEntity = modelMapper.map(storageDto, StorageEntity.class);
        StorageEntity savedStorageEntity = storageRepository.save(storageEntity);
        StorageDto savedStorageDto = modelMapper.map(savedStorageEntity, StorageDto.class);
        ResponseEntity<StorageDto> responseEntity = ResponseEntity.ok().body(savedStorageDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<StorageDto> update(StorageDto storageDto) {
        StorageEntity storageEntity = storageRepository.findById(storageDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Storage not found"));
        if (storageDto.getAdress() != null) {
            storageEntity.setAdress(storageDto.getAdress());
        }
        StorageEntity savedStorageEntity = storageRepository.save(storageEntity);
        StorageDto savedStorageDto = modelMapper.map(savedStorageEntity, StorageDto.class);
        ResponseEntity<StorageDto> responseEntity = ResponseEntity.ok().body(savedStorageDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<StorageDto> findById(Long id) {
        StorageEntity storageEntity = storageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Storage with id " + id + " not found"));
        StorageDto storageDto = modelMapper.map(storageEntity, StorageDto.class);
        ResponseEntity<StorageDto> responseEntity = ResponseEntity.ok().body(storageDto);
        return responseEntity;
    }

    @Override
    public ResponseEntity<List<StorageDto>> findAll() {
        List<StorageEntity> storageEntities = storageRepository.findAll();
        List<StorageDto> storageDTOs = storageEntities.stream()
                .map(storageEntity -> modelMapper.map(storageEntity, StorageDto.class))
                .toList();
        ResponseEntity<List<StorageDto>> responseEntity = ResponseEntity.ok().body(storageDTOs);
        return responseEntity;
    }

}

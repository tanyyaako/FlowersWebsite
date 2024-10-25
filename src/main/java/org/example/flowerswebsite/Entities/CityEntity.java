package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="city")
public class CityEntity extends BaseEntity{
    private String cityName;
    private Set<StorageEntity> storageEntitySet;

    public CityEntity(String cityName, Set<StorageEntity> storageEntitySet) {
        this.cityName = cityName;
        this.storageEntitySet = storageEntitySet;
    }

    protected CityEntity() {}

    @Column(name="cityName")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= StorageEntity.class,mappedBy = "city")
    public Set<StorageEntity> getStorageEntitySet() {
        return storageEntitySet;
    }

    public void setStorageEntitySet(Set<StorageEntity> storageEntitySet) {
        this.storageEntitySet = storageEntitySet;
    }
}

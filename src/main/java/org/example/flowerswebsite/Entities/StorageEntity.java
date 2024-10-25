package org.example.flowerswebsite.Entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="storage")
public class StorageEntity extends BaseEntity{
    private String adress;
    private CityEntity city;
    private Set<StorageContent> storageContents;
    private Set<OrderEntity> orderEntities;

    public StorageEntity(String adress, CityEntity city, Set<OrderEntity> orderEntities, Set<StorageContent> storageContents) {
        this.adress = adress;
        this.city = city;
        this.orderEntities = orderEntities;
        this.storageContents = storageContents;
    }

    protected StorageEntity() {}

    @Column(name="adress")
    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity=CityEntity.class)
    @JoinColumn(name="city_id")
    public CityEntity getCity() {
        return city;
    }
    public void setCity(CityEntity city) {
        this.city = city;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= StorageContent.class,mappedBy = "storage")
    public Set<StorageContent> getStorageContents() {
        return storageContents;
    }
    public void setStorageContents(Set<StorageContent> storageContents) {
        this.storageContents = storageContents;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH,targetEntity= OrderEntity.class,mappedBy = "storage")
    public Set<OrderEntity> getOrderEntities() {
        return orderEntities;
    }
    public void setOrderEntities(Set<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }
}

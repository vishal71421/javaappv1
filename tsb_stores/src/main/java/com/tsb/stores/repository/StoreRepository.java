package com.tsb.stores.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsb.entity.Stores;

public interface StoreRepository extends JpaRepository<Stores, Long>{
	
	Optional<Stores> findByStoreId(String storeId);


}

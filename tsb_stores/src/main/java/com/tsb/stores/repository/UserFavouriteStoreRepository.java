package com.tsb.stores.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tsb.entity.UserFavouriteStores;

@Repository
public interface UserFavouriteStoreRepository extends JpaRepository<UserFavouriteStores, Long>{
	
	UserFavouriteStores findByUserIdAndStoreId(String userId, String storeId);
	
	@Query("select tufs.storeId from UserFavouriteStores tufs where tufs.userId=:userId and tufs.isFavourite=:isFavourite")
	List<String> findByUserIdAndIsFavourite(String userId, Boolean isFavourite);
	
}

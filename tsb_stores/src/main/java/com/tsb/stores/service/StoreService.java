package com.tsb.stores.service;

import org.springframework.http.ResponseEntity;

import com.tsb.library.model.ApiResponse;
import com.tsb.stores.model.FavouriteStoreBean;
import com.tsb.stores.model.StoreLatLong;

public interface StoreService {

	ApiResponse saveStoreData();

	ResponseEntity<ApiResponse> getStores(StoreLatLong storeLatLong);
	
	ResponseEntity<ApiResponse> getStoresUnsec(StoreLatLong storeLatLong);

	ResponseEntity<ApiResponse> getStoreById(Long storeId);

	ResponseEntity<ApiResponse> addFavouriteStores(FavouriteStoreBean favouriteStoreBean);

	ResponseEntity<ApiResponse> favoriteStoreList(StoreLatLong storeLatLong);

}

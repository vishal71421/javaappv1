package com.tsb.stores.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsb.library.model.ApiResponse;
import com.tsb.stores.model.FavouriteStoreBean;
import com.tsb.stores.model.StoreLatLong;
import com.tsb.stores.service.StoreService;

@RestController
public class StoresController {
	private static final Logger logger = LogManager.getLogger(StoresController.class);
	
	@Autowired
	StoreService storeService;
	
	@PostMapping("/save/storeData")
	public ApiResponse getStoreData() {
		try {
			return storeService.saveStoreData();
		} catch (Exception e) {
			logger.error(" ", e);
			return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController saveStoreData"));
		}
	}
	
	@PostMapping("/app/store/get/v1")
	public ResponseEntity<ApiResponse> getStores(@RequestHeader(value = "User-Agent") String userAgent, @RequestHeader(value = "Authorization") String authorization,
			@Valid @RequestBody StoreLatLong storeLatLong) {
		try {
			return storeService.getStores(storeLatLong);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController getStores")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/app/unsec/store/get/v1")
	public ResponseEntity<ApiResponse> getStoresUnsec(@RequestHeader(value = "User-Agent") String userAgent,
			@Valid @RequestBody StoreLatLong storeLatLong) {
		try {
			return storeService.getStoresUnsec(storeLatLong);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController getStoresUnsec")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/app/store/view/v1")
	public ResponseEntity<ApiResponse> getStoreById(@RequestHeader(value = "User-Agent") String userAgent, @RequestHeader(value = "Authorization") String authorization,
			@RequestParam(value = "storeId", required = true) Long storeId) {
		try {
			return storeService.getStoreById(storeId);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController getStoreById")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/app/unsec/store/view/v1")
	public ResponseEntity<ApiResponse> getStoreByIdUnsec(@RequestHeader(value = "User-Agent") String userAgent,
			@RequestParam(value = "storeId", required = true) Long storeId) {
		try {
			return storeService.getStoreById(storeId);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController getStoreByIdUnsec")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/app/add/favoriteStores/v1")
	public ResponseEntity<ApiResponse> addFavouriteStores(@RequestHeader(value = "User-Agent") String userAgent, @RequestHeader(value = "Authorization") String authorization,
			@Valid @RequestBody FavouriteStoreBean favouriteStoreBean) {
		try {
			return storeService.addFavouriteStores(favouriteStoreBean);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController addFavouriteStores")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/app/favoriteStores/v1")
	public ResponseEntity<ApiResponse> appFavouriteStores(@RequestHeader(value = "User-Agent") String userAgent, @RequestHeader(value = "Authorization") String authorization,
			@Valid @RequestBody StoreLatLong storeLatLong) {
		try {
			return storeService.favoriteStoreList(storeLatLong);
		} catch (Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error at StoresController appFavouriteStores")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}

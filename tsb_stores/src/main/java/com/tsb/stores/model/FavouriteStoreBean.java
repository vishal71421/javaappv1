package com.tsb.stores.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavouriteStoreBean {
	@NotNull(message="storeId is required")
	private String storeId;
	@NotNull(message="isFavorite is required")
	private Boolean isFavorite;

}

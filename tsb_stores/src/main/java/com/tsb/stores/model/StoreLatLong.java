package com.tsb.stores.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StoreLatLong {
	@NotNull(message = "latitude is required")
	private Double lat;
	@NotNull(message = "longitude is required")
	private Double lng;
	private Double radius;
	private List<String> storeIds;
}

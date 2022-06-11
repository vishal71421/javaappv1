package com.tsb.stores.model;

import java.util.List;

import lombok.Data;

@Data
public class StoreDetailsOSBean {

    private int shopId;
    private String shopName;
    private String shopAddress;
    private String fssaiNumber;
    private double lat;
    private double lng;
    private double distanceFromPos;
    private String company;
    private Boolean active;
    private String email;
    private String storeContactPhone1;
    private String storeContactPhone2;
    private String storeContactPersonName;
    private ChannelDetails channelDetails;
    private AmenitiesOSBean amenities;
    private List<Shopimages> shopImages;
    private List<Workinghours> workingHours;
    private int estimatedDeliveryTime;
}

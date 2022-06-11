package com.tsb.stores.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreDetailsResponseBean {

    private String shopId;
    private String shopName;
    private String shopAddress;
    private String fssaiNumber;
    private double lat;
    private double lng;
    private double distanceFromPos;
    private String company;
    private String email;
    private String storeContactPhone1;
    private String storeContactPhone2;
    private String storeContactPersonName;
    private Boolean active;
    private ChannelDetails channelDetails;
    private List<AmenitiesResponseBean> amenities;
    private List<Shopimages> shopImages;
    private List<Workinghours> workingHours;
    private int estimatedDeliveryTime;
    private int driveIn;
    private Boolean isFavorite;
}

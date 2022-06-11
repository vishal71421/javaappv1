package com.tsb.stores.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChannelDetails {
	 private Pickup pickup;
	 public class Pickup {

		    @JsonProperty("startTime")
		    private String starttime;
		    @JsonProperty("endTime")
		    private String endtime;
		}
}

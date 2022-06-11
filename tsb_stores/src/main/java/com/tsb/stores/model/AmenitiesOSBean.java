package com.tsb.stores.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AmenitiesOSBean {

    @JsonProperty("curbSidePickUp")
    private Curbsidepickup curbsidepickup;
    @JsonProperty("driveThru")
    private Drivethru drivethru;
    @JsonProperty("deliveryAtHome")
    private Deliveryathome deliveryathome;
    @JsonProperty("outdoorSeating")
    private Outdoorseating outdoorseating;
    @JsonProperty("petFriendly")
    private Petfriendly petfriendly;
    @JsonProperty("open24Hours")
    private Open24hours open24hours;
    private Wifi wifi;
    @JsonProperty("dineIn")
    private Dinein dinein;
    public void setCurbsidepickup(Curbsidepickup curbsidepickup) {
         this.curbsidepickup = curbsidepickup;
     }
     public Curbsidepickup getCurbsidepickup() {
         return curbsidepickup;
     }

    public void setDrivethru(Drivethru drivethru) {
         this.drivethru = drivethru;
     }
     public Drivethru getDrivethru() {
         return drivethru;
     }

    public void setDeliveryathome(Deliveryathome deliveryathome) {
         this.deliveryathome = deliveryathome;
     }
     public Deliveryathome getDeliveryathome() {
         return deliveryathome;
     }

    public void setOutdoorseating(Outdoorseating outdoorseating) {
         this.outdoorseating = outdoorseating;
     }
     public Outdoorseating getOutdoorseating() {
         return outdoorseating;
     }

    public void setPetfriendly(Petfriendly petfriendly) {
         this.petfriendly = petfriendly;
     }
     public Petfriendly getPetfriendly() {
         return petfriendly;
     }

    public void setOpen24hours(Open24hours open24hours) {
         this.open24hours = open24hours;
     }
     public Open24hours getOpen24hours() {
         return open24hours;
     }

    public void setWifi(Wifi wifi) {
         this.wifi = wifi;
     }
     public Wifi getWifi() {
         return wifi;
     }

    public void setDinein(Dinein dinein) {
         this.dinein = dinein;
     }
     public Dinein getDinein() {
         return dinein;
     }

     public class Curbsidepickup {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Drivethru {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Deliveryathome {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Outdoorseating {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Petfriendly {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Open24hours {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }
     }
     
     public class Wifi {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }

     }
     
     public class Dinein {

         private String icon;
         private boolean status;
         public void setIcon(String icon) {
              this.icon = icon;
          }
          public String getIcon() {
              return icon;
          }

         public void setStatus(boolean status) {
              this.status = status;
          }
          public boolean getStatus() {
              return status;
          }

     }
}
package com.tsb.stores.service.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsb.entity.Stores;
import com.tsb.entity.UserFavouriteStores;
import com.tsb.enums.StatusType;
import com.tsb.library.model.ApiResponse;
import com.tsb.stores.model.AmenitiesResponseBean;
import com.tsb.stores.model.FavouriteStoreBean;
import com.tsb.stores.model.StoreDetailsOSBean;
import com.tsb.stores.model.StoreDetailsResponseBean;
import com.tsb.stores.model.StoreLatLong;
import com.tsb.stores.repository.StoreRepository;
import com.tsb.stores.repository.UserFavouriteStoreRepository;
import com.tsb.stores.service.StoreService;
import com.tsb.user.biz.service.LoginBiz;
import com.tsb.user.repository.UserProfileRepository;

@Service
public class StoreServiceImpl implements StoreService{
	
	private static final Logger logger = LogManager.getLogger(StoreServiceImpl.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${orderserve.base.url}")
	private String orderserveBaseUrl;
	
	@Value("${orderserve.active.stores.url}")
	private String orderserveUrl;
	
	@Value("${orderserve.stores.url}")
	private String orderserveStoresUrl;
	
	@Value("${orderserve.getStoreById.url}")
	private String orderserveGetStoreByIdUrl;
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	UserFavouriteStoreRepository userFavouriteStoreRepository;
	
	@Value("${tsb.stores.radius.value}")
	private Double radius;
	
	@Autowired
	LoginBiz userService;

	@Override
	public ApiResponse saveStoreData() {
		StoreLatLong storeLatLong = null;
		Stores store = null;
		ObjectMapper mapper = null;
		HttpHeaders headers = null;
		JSONObject json = null;
		int counter;
		try {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			storeLatLong = new StoreLatLong();
			storeLatLong.setLat(18.956428);
			storeLatLong.setLng(72.8324487);
			storeLatLong.setRadius(10.00);

			HttpEntity<?> entity = new HttpEntity<>(storeLatLong,headers);
			ResponseEntity<Object[]> responseEntity = restTemplate.exchange(orderserveBaseUrl+orderserveUrl, HttpMethod.GET, entity, Object[].class);
			if(responseEntity.getStatusCode()==HttpStatus.OK) {
				mapper = new ObjectMapper();
				Object[] objects = responseEntity.getBody();
				if(null!=objects) {
				for (counter = 0; counter < objects.length; counter++) {
					json=new JSONObject(mapper.writeValueAsString(objects[counter]));
					Optional<Stores> storeOpt=storeRepository.findByStoreId(json.get("shopId").toString());
					if(storeOpt.isPresent()) {
						storeOpt.get().setStoreDetails(json.toString());
						storeOpt.get().setLatitude(json.get("lat").toString());
						storeOpt.get().setLongitude(json.get("lng").toString());
						storeRepository.save(storeOpt.get());
					} else {
					store = new Stores();
					store.setCreatedBy(1L);
					store.setModifiedBy(1L);
					store.setCreatedTime(new Date());
					
					store.setModifiedTime(new Date());
					store.setStatus(StatusType.ONLINE);
					store.setStoreId(json.get("shopId").toString());
					if(json.has("lat")) {
					store.setLatitude(json.get("lat").toString());
					} if(json.has("lng")) {
					store.setLongitude(json.get("lng").toString());
					}
					store.setStoreDetails(json.toString());
					storeRepository.save(store);
					}
				 }
				}
				return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), List.of("Success"));
			} else {
				return new ApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
						List.of("Invalid Credentials"));
			}
			
		} catch(Exception e) {
			logger.error(" ", e);
			return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error @ saveStoreData"));
		}
	}

	@Override
	public ResponseEntity<ApiResponse>  getStores(StoreLatLong storeLatLong) {
		HttpHeaders headers = null;
		StoreDetailsOSBean[] storeDetailsOSBean = null;
		StoreDetailsResponseBean storeDetailsResponseBean = null;
		List<StoreDetailsResponseBean> storeDetailsResponseList = null;
		UserFavouriteStores userFavouriteStores = null;
		String userId=null;
		List<AmenitiesResponseBean> amenitiesResponseList = new ArrayList<>();
		try {
			userId=userService.findLoggedInUser();
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			storeLatLong.setRadius(radius);
			HttpEntity<?> entity = new HttpEntity<>(storeLatLong,headers);
			ResponseEntity<StoreDetailsOSBean[]> responseEntity = restTemplate.exchange(orderserveBaseUrl+orderserveStoresUrl, HttpMethod.POST, entity, StoreDetailsOSBean[].class);
			if(responseEntity.getStatusCode()==HttpStatus.OK) {
				storeDetailsOSBean = responseEntity.getBody();
				storeDetailsResponseList = new ArrayList<>();
				
				for(StoreDetailsOSBean storeDetailsBean : storeDetailsOSBean) {
					amenitiesResponseList = setAmenities(storeDetailsBean);
					userFavouriteStores = userFavouriteStoreRepository.findByUserIdAndStoreId(userId, String.valueOf(storeDetailsBean.getShopId()));
					storeDetailsResponseBean = new StoreDetailsResponseBean(String.valueOf(storeDetailsBean.getShopId()), storeDetailsBean.getShopName(), storeDetailsBean.getShopAddress(), 
							storeDetailsBean.getFssaiNumber(), storeDetailsBean.getLat(), storeDetailsBean.getLng(), storeDetailsBean.getDistanceFromPos(), storeDetailsBean.getCompany(), 
							storeDetailsBean.getEmail(), storeDetailsBean.getStoreContactPhone1(), storeDetailsBean.getStoreContactPhone2(), storeDetailsBean.getStoreContactPersonName(), storeDetailsBean.getActive(), 
							storeDetailsBean.getChannelDetails(), amenitiesResponseList, storeDetailsBean.getShopImages(), 
							null!=storeDetailsBean.getWorkingHours()?storeDetailsBean.getWorkingHours():null, storeDetailsBean.getEstimatedDeliveryTime(), 10, null!=userFavouriteStores?userFavouriteStores.getIsFavourite():false);
					storeDetailsResponseList.add(storeDetailsResponseBean);
				}
				return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), 
						storeDetailsResponseList), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), 
						List.of("No stores found")), HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e) {
			logger.error(" ", e);
		 return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error @ getStores")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public ResponseEntity<ApiResponse>  getStoresUnsec(StoreLatLong storeLatLong) {
		HttpHeaders headers = null;
		StoreDetailsOSBean[] storeDetailsOSBean = null;
		StoreDetailsResponseBean storeDetailsResponseBean = null;
		List<StoreDetailsResponseBean> storeDetailsResponseList = null;
		List<AmenitiesResponseBean> amenitiesResponseList = new ArrayList<>();
		try {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			storeLatLong.setRadius(radius);
			HttpEntity<?> entity = new HttpEntity<>(storeLatLong,headers);
			ResponseEntity<StoreDetailsOSBean[]> responseEntity = restTemplate.exchange(orderserveBaseUrl+orderserveStoresUrl, HttpMethod.POST, entity, StoreDetailsOSBean[].class);
			if(responseEntity.getStatusCode()==HttpStatus.OK) {
				storeDetailsOSBean = responseEntity.getBody();
				storeDetailsResponseList = new ArrayList<>();
				for(StoreDetailsOSBean storeDetailsBean : storeDetailsOSBean) {
					amenitiesResponseList = setAmenities(storeDetailsBean);
					storeDetailsResponseBean = new StoreDetailsResponseBean(String.valueOf(storeDetailsBean.getShopId()), storeDetailsBean.getShopName(), storeDetailsBean.getShopAddress(), 
							storeDetailsBean.getFssaiNumber(), storeDetailsBean.getLat(), storeDetailsBean.getLng(), storeDetailsBean.getDistanceFromPos(), storeDetailsBean.getCompany(), 
							storeDetailsBean.getEmail(), storeDetailsBean.getStoreContactPhone1(), storeDetailsBean.getStoreContactPhone2(), storeDetailsBean.getStoreContactPersonName(), storeDetailsBean.getActive(), 
							storeDetailsBean.getChannelDetails(), amenitiesResponseList, storeDetailsBean.getShopImages(), 
							null!=storeDetailsBean.getWorkingHours()?storeDetailsBean.getWorkingHours():null, storeDetailsBean.getEstimatedDeliveryTime(), 10, false);
					storeDetailsResponseList.add(storeDetailsResponseBean);
				}
				return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), 
						storeDetailsResponseList), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), 
						List.of("No stores found")), HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error @ getStoresUnsec")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<ApiResponse> getStoreById(Long storeId) {
		String getStoreByIdURI = null;
		StoreDetailsOSBean storeDetailsOSBean = null;
		StoreDetailsResponseBean storeDetailsResponseBean = null;
		List<AmenitiesResponseBean> amenitiesResponseList = new ArrayList<>();
		try {
		Optional<Stores> storeOpt=storeRepository.findByStoreId(String.valueOf(storeId));
		if(storeOpt.isPresent()) {
		getStoreByIdURI=orderserveBaseUrl+orderserveGetStoreByIdUrl+storeId;
		ResponseEntity<StoreDetailsOSBean> responseEntity = restTemplate.exchange(getStoreByIdURI, HttpMethod.GET, null, StoreDetailsOSBean.class);
		if(responseEntity.getStatusCode()==HttpStatus.OK) {
			storeDetailsOSBean = responseEntity.getBody();
			if(null!=storeDetailsOSBean){
			amenitiesResponseList = setAmenities(storeDetailsOSBean);
			storeDetailsResponseBean = new StoreDetailsResponseBean(String.valueOf(storeDetailsOSBean.getShopId()), storeDetailsOSBean.getShopName(), storeDetailsOSBean.getShopAddress(), 
					storeDetailsOSBean.getFssaiNumber(), storeDetailsOSBean.getLat(), storeDetailsOSBean.getLng(), storeDetailsOSBean.getDistanceFromPos(), storeDetailsOSBean.getCompany(), 
					storeDetailsOSBean.getEmail(), storeDetailsOSBean.getStoreContactPhone1(), storeDetailsOSBean.getStoreContactPhone2(), storeDetailsOSBean.getStoreContactPersonName(), storeDetailsOSBean.getActive(), 
					storeDetailsOSBean.getChannelDetails(), amenitiesResponseList, storeDetailsOSBean.getShopImages(), 
					null!=storeDetailsOSBean.getWorkingHours()?storeDetailsOSBean.getWorkingHours():null, storeDetailsOSBean.getEstimatedDeliveryTime(), 10, false);
			}
		  }
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), storeDetailsResponseBean),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					List.of("Invalid storeId")),HttpStatus.BAD_REQUEST);
		}
	} catch(Exception e) {
		logger.error(" ", e);
		return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
				List.of("Internal Server Error @ getStoreById")),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	@Override
	public ResponseEntity<ApiResponse>  addFavouriteStores(FavouriteStoreBean favouriteStoreBean) {
		String userId=null;
		UserFavouriteStores userFavouriteStore = null;
		try {
			Optional<Stores> storeOpt=storeRepository.findByStoreId(String.valueOf(favouriteStoreBean.getStoreId()));
			if(storeOpt.isPresent()) {
			userId=userService.findLoggedInUser();
			if(null!=userId) {
			userFavouriteStore = userFavouriteStoreRepository.findByUserIdAndStoreId(userId, favouriteStoreBean.getStoreId());
			if(null!=userFavouriteStore) {
				userFavouriteStore.setModifiedBy(1L);
				userFavouriteStore.setModifiedTime(new Date());
				userFavouriteStore.setIsFavourite(favouriteStoreBean.getIsFavorite());
				userFavouriteStoreRepository.save(userFavouriteStore);
				
			} else {
				userFavouriteStore = new UserFavouriteStores();
				userFavouriteStore.setCreatedBy(1L);
				userFavouriteStore.setModifiedBy(1L);
				userFavouriteStore.setCreatedTime(new Date());
				userFavouriteStore.setModifiedTime(new Date());
				userFavouriteStore.setStatus(StatusType.ONLINE);
				userFavouriteStore.setUserId(userId);
				userFavouriteStore.setStoreId(favouriteStoreBean.getStoreId());
				userFavouriteStore.setIsFavourite(favouriteStoreBean.getIsFavorite());
				userFavouriteStoreRepository.save(userFavouriteStore);
			  }
			}
			return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), List.of("Success")),HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
						List.of("Invalid storeId")),HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error @ addFavouriteStores")),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	}
	
	@Override
	public ResponseEntity<ApiResponse> favoriteStoreList(StoreLatLong storeLatLong) {
		String userId=null;
		HttpHeaders headers = null;
		StoreDetailsOSBean[] storeDetailsOSBean = null;
		StoreDetailsResponseBean favouriteStoreBean = null;
		List<StoreDetailsResponseBean> favouriteStoreList = new ArrayList<>();
		List<AmenitiesResponseBean> amenitiesResponseList = new ArrayList<>();
		List<String> favouriteStoreIds=null;
		try {
			userId=userService.findLoggedInUser();
			favouriteStoreIds= userFavouriteStoreRepository.findByUserIdAndIsFavourite(userId, true);
			if(null!=favouriteStoreIds && !favouriteStoreIds.isEmpty()) {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			storeLatLong.setRadius(radius);
			storeLatLong.setStoreIds(favouriteStoreIds);
			HttpEntity<?> entity = new HttpEntity<>(storeLatLong,headers);
			ResponseEntity<StoreDetailsOSBean[]> responseEntity = restTemplate.exchange(orderserveBaseUrl+orderserveStoresUrl, HttpMethod.POST, entity, StoreDetailsOSBean[].class);
			if(responseEntity.getStatusCode()==HttpStatus.OK) {
				storeDetailsOSBean = responseEntity.getBody();
				for(StoreDetailsOSBean storeDetailsBean : storeDetailsOSBean) {
					amenitiesResponseList = setAmenities(storeDetailsBean);
					favouriteStoreBean = new StoreDetailsResponseBean(String.valueOf(storeDetailsBean.getShopId()), storeDetailsBean.getShopName(), storeDetailsBean.getShopAddress(), 
							storeDetailsBean.getFssaiNumber(), storeDetailsBean.getLat(), storeDetailsBean.getLng(), storeDetailsBean.getDistanceFromPos(), storeDetailsBean.getCompany(), 
							storeDetailsBean.getEmail(), storeDetailsBean.getStoreContactPhone1(), storeDetailsBean.getStoreContactPhone2(), storeDetailsBean.getStoreContactPersonName(), storeDetailsBean.getActive(), 
							storeDetailsBean.getChannelDetails(), amenitiesResponseList, storeDetailsBean.getShopImages(), 
							null!=storeDetailsBean.getWorkingHours()?storeDetailsBean.getWorkingHours():null, storeDetailsBean.getEstimatedDeliveryTime(), 10, true);
					favouriteStoreList.add(favouriteStoreBean);
			   }
			 }
			}
			return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), favouriteStoreList),HttpStatus.OK);
		} catch(Exception e) {
			logger.error(" ", e);
			return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					List.of("Internal Server Error @ favoriteStoreList")),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private List<AmenitiesResponseBean> setAmenities(StoreDetailsOSBean storeDetailsBean) {
		AmenitiesResponseBean amenitiesResponseBean = null;
		List<AmenitiesResponseBean> amenitiesResponseList = new ArrayList<>();
		try {
		if(null!=storeDetailsBean.getAmenities().getCurbsidepickup() && storeDetailsBean.getAmenities().getCurbsidepickup().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Curb Side Pickup", storeDetailsBean.getAmenities().getCurbsidepickup().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getDeliveryathome() && storeDetailsBean.getAmenities().getDeliveryathome().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Delivery At home", storeDetailsBean.getAmenities().getDeliveryathome().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getDinein() && storeDetailsBean.getAmenities().getDinein().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Dine In", storeDetailsBean.getAmenities().getDinein().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getDrivethru() && storeDetailsBean.getAmenities().getDrivethru().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Drive thru", storeDetailsBean.getAmenities().getDrivethru().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getOutdoorseating() && storeDetailsBean.getAmenities().getOutdoorseating().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Outdoor Seating", storeDetailsBean.getAmenities().getOutdoorseating().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getOpen24hours() && storeDetailsBean.getAmenities().getOpen24hours().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Open 24 hours", storeDetailsBean.getAmenities().getOpen24hours().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		if(null!=storeDetailsBean.getAmenities().getWifi() && storeDetailsBean.getAmenities().getWifi().getStatus()) {
			amenitiesResponseBean = new AmenitiesResponseBean("Wifi", storeDetailsBean.getAmenities().getWifi().getIcon());
			amenitiesResponseList.add(amenitiesResponseBean);
		}
		return amenitiesResponseList;
		} catch(Exception e) {
			return amenitiesResponseList;
		}
	}

}
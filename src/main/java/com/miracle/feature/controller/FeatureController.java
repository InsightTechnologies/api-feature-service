package com.miracle.feature.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.common.api.bean.Feature;
import com.miracle.common.bean.APIMicroServiceBean;
import com.miracle.common.controller.APIMicroService;
import com.miracle.common.response.FeatureResponse;
import com.miracle.feature.constants.FeatureConstants;
import com.miracle.feature.exception.FeatureErrorCode;
import com.miracle.feature.exception.FeatureException;

@RestController
@RequestMapping(value = "/masterBot/project")
public class FeatureController extends APIMicroService {
	public static final Logger logger = LoggerFactory.getLogger(FeatureController.class);
	@Autowired
	RestTemplate restTemplate;

	// API_M1
	@PostMapping("/features")
	@ResponseBody
	public ResponseEntity<FeatureResponse> runService(@RequestBody APIMicroServiceBean apiMicroServiceBean) {
		FeatureResponse response = new FeatureResponse();

		List<Feature> filteredFeatures = null;
		try {
			String iceScrumURLPrefix = getIceScrumURLPrefix();

			Map<String, String> headerDetails = commonUtil.getHeaderDetails();
			List<MediaType> acceptableMediaTypes = commonUtil.getAcceptableMediaTypes();
			String url = iceScrumURLPrefix + apiMicroServiceBean.getProjectName() + FeatureConstants.FEATURE_PATH;
			String featureDetails = commonUtil.getDetails(url, headerDetails, acceptableMediaTypes);
			logger.info("Extracted features from icescum ::" + featureDetails);
			List<Feature> extracetdFeaturesList = new ObjectMapper().readValue(featureDetails,
					new TypeReference<List<Feature>>() {
					});
			filteredFeatures = getFilteredFeatureList(apiMicroServiceBean, extracetdFeaturesList);
			response.setObject(filteredFeatures);
			response.setSuccess(true);
			logger.info("After applying filters on features ::" + filteredFeatures);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error("Getting exception in retrieve and filtering feature , Exception desciption :: "
					+ exception.getMessage(), exception);
//			throw new FeatureException(
//					"Getting exception in retrieve and filtering feature , Exception desciption :: "
//							+ exception.getMessage(),
//					exception, FeatureErrorCode.UNKNOWN_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);

			response.setObject("Getting exception in retrieve and filtering feature , Exception desciption :: "
					+ exception.getMessage());
			response.setSuccess(false);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param apiMicroServiceBean
	 * @param extracetdFeaturesList
	 * @return
	 * @throws Exception
	 */
	private List<Feature> getFilteredFeatureList(APIMicroServiceBean apiMicroServiceBean,
			List<Feature> extracetdFeaturesList) throws Exception {
		List<Feature> filteredFeatures = new ArrayList<Feature>();

		if (extracetdFeaturesList != null && extracetdFeaturesList.size() > 0) {
			if (apiMicroServiceBean.getFeatureStateList() != null) {

				for (Feature feature : extracetdFeaturesList) {
					for (Integer state : apiMicroServiceBean.getFeatureStateList()) {
						if (state == feature.getState()) {
							logger.info(
									"Feature ID:: " + feature.getId() + "\t Uid :: " + feature.getUid() + "\t Name :: "
											+ feature.getName() + "\t State of Feature  :: " + feature.getState());
							filteredFeatures.add(applyFilterOnFeature(apiMicroServiceBean, feature));
//								break;
						}
					}

				}
			} else {
				logger.error("Invalid feature states in request");
				throw new FeatureException("Invalid states in request", FeatureErrorCode.INVALID_STATES);
			}

		} else {
			logger.error("No features found in icescurm");
			throw new FeatureException("No features found in icescurm", FeatureErrorCode.EMPTY_FEATURE);
		}
		return filteredFeatures;
	}

	/**
	 * 
	 * @param apiMicroServiceBean
	 * @param feature
	 * @return
	 * @throws Exception
	 */
	private Feature applyFilterOnFeature(APIMicroServiceBean apiMicroServiceBean, Feature feature) throws Exception {
		Feature filteredFeature = new Feature();
		List<String> featureStatus = apiMicroServiceBean.getFeatureStatus();
		if (featureStatus != null) {
			logger.info("Applying filter " + featureStatus);
			for (String featureProperty : featureStatus) {
				switch (featureProperty) {
				case "class":
					filteredFeature.setClassName(feature.getClassName());
					break;
				case "id":
					filteredFeature.setId(feature.getId());
					break;
				case "activities_count":
					filteredFeature.setActivities_count(feature.getActivities_count());
					break;
				case "attachments_count":
					filteredFeature.setAttachments_count(feature.getAttachments_count());
					break;
				case "backlog":
					filteredFeature.setBacklog(feature.getBacklog());
					break;
				case "color":
					filteredFeature.setColor(feature.getColor());
					break;
				case "comments_count":
					filteredFeature.setComments_count(feature.getComments_count());
					break;
				case "dateCreated":
					filteredFeature.setDateCreated(feature.getDateCreated());
					break;
				case "description":
					filteredFeature.setDescription(feature.getDescription());
					break;
				case "lastUpdated":
					filteredFeature.setLastUpdated(feature.getLastUpdated());
					break;
				case "name":
					filteredFeature.setName(feature.getName());
					break;
				case "notes":
					filteredFeature.setNotes(feature.getNotes());
					break;
				case "parentRelease":
					filteredFeature.setParentRelease(feature.getParentRelease());
					break;
				case "rank":
					filteredFeature.setRank(feature.getRank());
					break;
				case "stories_ids":
					filteredFeature.setStories_ids(feature.getStories_ids());
					break;
				case "todoDate":
					filteredFeature.setTodoDate(feature.getTodoDate());
					break;

				case "type":
					filteredFeature.setType(feature.getType());
					break;
				case "uid":
					filteredFeature.setUid(feature.getUid());
					break;
				case "value":
					filteredFeature.setValue(feature.getValue());
					break;
				case "countDoneStories":
					filteredFeature.setCountDoneStories(feature.getCountDoneStories());
					break;
				case "state":
					filteredFeature.setState(feature.getState());
					break;
				case "effort":
					filteredFeature.setEffort(feature.getEffort());
					break;
				case "inProgressDate":
					filteredFeature.setInProgressDate(feature.getInProgressDate());
					break;
				case "project":
					filteredFeature.setProject(feature.getProject());
					break;
				case "notes_html":
					filteredFeature.setNotes_html(feature.getNotes_html());
					break;
				default:
					break;
				}
			}
		} else {
			logger.error("Invalid Feature List in request");
			throw new FeatureException("Invalid Feature List in request", FeatureErrorCode.INVALID_FEATURE);
		}

//		 Gson gson = new Gson();
//		 System.out.println(gson.toJson(filteredFeature));
		// System.out.println(new ObjectMapper().writeValueAsString(filteredFeature));
		return filteredFeature;
	}
}

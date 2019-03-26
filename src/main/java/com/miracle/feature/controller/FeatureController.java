package com.miracle.feature.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.common.filter.Field;
import com.miracle.feature.response.FeatureResponse;

@RestController
public class FeatureController {

	@GetMapping(value = "/features")
	public ResponseEntity<FeatureResponse> buildFeatures(
			@RequestParam(value = "featureStates") List<Object> featureStates,
			@RequestParam(value = "projectName") String projectName,
			@RequestParam(value = "responseFields") List<Field> responseFields) {
		// Invoke IceScrum, retrieve feature JSON response,
		// Build FeatureJson based on responseFields Enum
		// Filter features based on featureStates with integers as Status values
		FeatureResponse response = new FeatureResponse();
		// send filtered FeatureJson object using FeatureResponse

		return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);
	}

}

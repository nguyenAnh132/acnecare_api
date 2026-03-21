package com.acnecare.api.patient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import com.acnecare.api.patient.service.AcnePredictionService;

@RestController
@RequestMapping("/acne-predictions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AcnePredictionController {

    AcnePredictionService acnePredictionService;

    

}

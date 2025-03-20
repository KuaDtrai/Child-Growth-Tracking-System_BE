package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.FeedbackDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/feedback")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // get all feedback by doctor id
    // http://localhost:8080/api/v1/feedback/getAllFeedbackByDoctorId
//    @GetMapping
//    public ResponseEntity<ResponseObject> getAllFeedbackByDoctorId() {
//        return feedbackService.getAllFeedbackByDoctorId();
//    }

    // create feedback by doctor id
    // http://localhost:8080/api/v1/feedback/createFeedback
    @PostMapping("/createFeedback")
    public ResponseEntity<ResponseObject> createFeedback(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long parentId,
            @RequestBody(required = false) FeedbackDTO feedback) {
        return feedbackService.createFeedback(doctorId,parentId, feedback);
    }



}

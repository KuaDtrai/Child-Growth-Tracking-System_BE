package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.FeedbackDTO;
import G5_SWP391.ChildGrownTracking.responses.FeedbackResponseDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("")
    public ResponseEntity<ResponseObject> memberFeedback(
    ){
        List<FeedbackResponseDTO> feedbackResponseDTOS = feedbackService.getAllFeedback();
        if (!feedbackResponseDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Feedback found", feedbackResponseDTOS));
        }else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "No feedback found", null));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<ResponseObject> doctorFeedback(
            @Valid @PathVariable("id") Long id
    ){
        List<FeedbackResponseDTO> feedbackResponseDTOS = feedbackService.getAllFeedbackByDoctor(id);
        if (!feedbackResponseDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Feedback found", feedbackResponseDTOS));
        }else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "No feedback found", null));
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<ResponseObject> memberFeedback(
            @Valid @PathVariable("id") Long id
    ){
        List<FeedbackResponseDTO> feedbackResponseDTOS = feedbackService.getAllFeedbackByUser(id);
        if (!feedbackResponseDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Feedback found", feedbackResponseDTOS));
        }else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "No feedback found", null));
    }

    @GetMapping("/doctor/rating/{id}")
    public ResponseEntity<ResponseObject> getDoctorAverageRating(
            @Valid @PathVariable("id") Long id
    ){
        float rating = feedbackService.getDoctorRating(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Rating", rating));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteFeedback(
            @Valid @PathVariable("id") Long id
    ){
        boolean isDeleted = feedbackService.deleteFeedbackById(id);
        if (isDeleted) return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Feedback deleted", null));
        else return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "Feedback deleted", null));
    }

}

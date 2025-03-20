package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.FeedbackDTO;
import G5_SWP391.ChildGrownTracking.models.Feedback;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.FeedbackRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.FeedbackResponseDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ResponseObject> createFeedback(Long doctorId,Long parentId, FeedbackDTO feedback) {

        if(doctorId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Doctor Id is required", null)
            );
        }
        if(parentId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Parent Id is required", null)
            );
        }
        if(feedback.getDiscription() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Discription is required", null)
            );
        }
        if(feedback.getRating() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Rating is required", null)
            );
        }

        Optional<User> parentOptional = userRepository.findByIdAndStatusIsTrue(parentId);

        if(!parentOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Parent not found", null)
            );
        }

        User parent = parentOptional.get();

        if(parent.getRole() != role.MEMBER){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Parent not found", null)
            );
        }



        Optional<User> doctorOptional = userRepository.findByIdAndStatusIsTrue(doctorId);

        if(!doctorOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Doctor not found", null)
            );
        }

        User doctor = doctorOptional.get();

        if(doctor.getRole() != role.DOCTOR){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Doctor not found", null)
            );
        }

        long feedbackCount = feedbackRepository.countByUserAndDoctor(parentId, doctorId);

        if (feedbackCount > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Bạn chỉ có thể gửi feedback duy nhất một lần!", null)
            );
        }


        Feedback newFeedback = new Feedback(
                parent,
                doctor,
                feedback.getRating(),
                feedback.getDiscription()
        );

        feedbackRepository.save(newFeedback);
        FeedbackResponseDTO responseDTO = new FeedbackResponseDTO(
                newFeedback.getId(),
                newFeedback.getDescription(),
                newFeedback.getRating(),
                parent.getUserName(),
                doctor.getUserName()
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("success", "Feedback created successfully", responseDTO)
        );

    }

    public List<FeedbackResponseDTO> getAllFeedback() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackResponseDTO> feedbackResponseDTOs = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponseDTO responseDTO = new FeedbackResponseDTO(feedback.getId(), feedback.getDescription(), feedback.getRating(), feedback.getUser().getUserName(), feedback.getDoctor().getUserName());
            feedbackResponseDTOs.add(responseDTO);
        }
        return feedbackResponseDTOs;
    }

    public List<FeedbackResponseDTO> getAllFeedbackByDoctor(Long doctorId) {
//        Optional<User> user = userRepository.findByIdAndStatusIsTrue(doctorId);
        List<Feedback> feedbacks = feedbackRepository.findByDoctorId(doctorId);
        List<FeedbackResponseDTO> feedbackResponseDTOs = new LinkedList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponseDTO responseDTO = new FeedbackResponseDTO(feedback.getId(), feedback.getDescription(), feedback.getRating(), feedback.getUser().getUserName(), feedback.getDoctor().getUserName());
            feedbackResponseDTOs.add(responseDTO);
        }
        return feedbackResponseDTOs;
    }

    public List<FeedbackResponseDTO> getAllFeedbackByUser(Long userId) {
        List<Feedback> feedbacks = feedbackRepository.findByUserId(userId);
        List<FeedbackResponseDTO> feedbackResponseDTOs = new LinkedList<>();
        for (Feedback feedback : feedbacks) {
            FeedbackResponseDTO responseDTO = new FeedbackResponseDTO(feedback.getId(), feedback.getDescription(), feedback.getRating(), feedback.getUser().getUserName(), feedback.getUser().getUserName());
            feedbackResponseDTOs.add(responseDTO);
        }
        return feedbackResponseDTOs;
    }

    public int getDoctorRating(Long doctorId) {
        List<Feedback> feedbacks = feedbackRepository.findByDoctorId(doctorId);
        int rating = 0;
        for (Feedback feedback : feedbacks) {
            rating += feedback.getRating().getValue();
        }
        rating = rating / feedbacks.size();
        return rating;
    }
}

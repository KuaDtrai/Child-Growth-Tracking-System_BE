package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Post;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.PostRepository;
import G5_SWP391.ChildGrownTracking.responses.PostResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChildRepository childRepository;

    public ResponseEntity <ResponseObject> findByChild(@RequestParam Long childId) {
        if (!childRepository.findById(childId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }

        Child child = childRepository.findByIdAndStatusIsTrue(childId).get();
        List<PostResponse> posts = postRepository.findByChild(child);
        if(posts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "Posts found.", posts));

    }

}

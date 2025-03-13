package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.PostDTO;
import G5_SWP391.ChildGrownTracking.models.Post;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/v1/post")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/getAllPostByChildId")
    public ResponseEntity<ResponseObject> getAllPostByChildId(@RequestParam("childId") Long childId) {
        return postService.findByChildId(childId);
    }

    @PostMapping("/createPost")
    public ResponseEntity<ResponseObject> createPost(@RequestBody PostDTO post) {
        return postService.createPost(post);
    }

    @PutMapping("/deletePost")
    public ResponseEntity<ResponseObject> deletePost(@RequestParam("postId") Long postId) {
        return postService.deletePost(postId);
    }


}

package G5_SWP391.ChildGrownTracking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G5_SWP391.ChildGrownTracking.dtos.PostDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.PostService;

@RequestMapping(path = "/api/v1/post")
@RestController
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/getAllPostByChildId/{id}")
    public ResponseEntity<ResponseObject> getAllPostByChildId(@PathVariable("id") Long id) {
        return postService.findByChildId(id);
    }

    @PostMapping("/createPost")
    public ResponseEntity<ResponseObject> createPost(@RequestBody(required = false) PostDTO post) {
        return postService.createPost(post);
    }

    @PutMapping("/deletePost/{id}")
    public ResponseEntity<ResponseObject> deletePost(@PathVariable("id") Long id) {
        return postService.deletePost(id);
    }


}

package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.PostDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Post;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.PostRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.PostResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity <ResponseObject> findByChildId(@RequestParam Long childId) {

        Child child = childRepository.findByIdAndStatusIsTrue(childId).orElse(null);
        if (child == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }


        List<Post> posts = postRepository.findByChild(child);

        if(posts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }
        List<PostResponse> postResponses = new ArrayList<>();
        for( Post post : posts){
            if(post.isStatus()){
                PostResponse Response = new PostResponse(
                        post.getId(),
                        post.getUser().getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getCreatedDate(),
                        post.isStatus()
                );
                postResponses.add(Response);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "Posts found.", postResponses));

    }

    public ResponseEntity<ResponseObject> createPost(PostDTO postDTO) {

        if( postDTO.getUserId() == null || postDTO.getChildId() == null || postDTO.getTitle() == null || postDTO.getDescription() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Missing required fields.", null));
        }


        Optional<User> userOptional = userRepository.findByIdAndStatusIsTrue(postDTO.getUserId());
        if (!userOptional.isPresent() || !userOptional.get().isStatus()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "User not found or inactive.", null));
        }

        User user = userOptional.get();

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(postDTO.getChildId());
        if (!childOptional.isPresent() || !childOptional.get().isStatus()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child not found or inactive.", null));
        }

        Child child = childOptional.get();

        if(!user.getRole().equals(role.DOCTOR) || !user.getRole().equals(role.MEMBER)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "User is not doctor or parent.", null));
        }

        if(user.getRole().equals(role.DOCTOR)){
            if(!user.getChildren2().contains(child)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("fail", "User is not doctor of this child.", null));
            }
        }


        if(user.getRole().equals(role.MEMBER)){
            if(!user.getChildren().contains(child)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("fail", "User is not parent of this child.", null));
            }
        }


        // Tạo Post mới
        Post newPost = new Post(
                user,
                child,
                postDTO.getTitle(),
                postDTO.getDescription(),
                LocalDateTime.now(),
                true // Mặc định bài viết mới sẽ active
        );

        postRepository.save(newPost); // Lưu vào database

        PostResponse p = new PostResponse(
                newPost.getId(),
                newPost.getUser().getId(),
                newPost.getTitle(),
                newPost.getDescription(),
                newPost.getCreatedDate(),
                newPost.isStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObject("success", "Post created successfully", p));

    }

    public ResponseEntity<ResponseObject> deletePost(Long postId) {
        // Tìm Post theo ID
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Post with ID " + postId + " not found.", null));
        }

        Post post = postOptional.get();

        // Nếu Post đã bị vô hiệu hóa rồi thì báo lỗi
        if (!post.isStatus()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Post is already deleted.", null));
        }

        // Cập nhật status về false
        post.setStatus(false);
        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("success", "Post deleted successfully.", null));
    }

}

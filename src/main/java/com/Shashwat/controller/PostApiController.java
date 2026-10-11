package com.Shashwat.controller;

import com.Shashwat.model.Comment;
import com.Shashwat.model.Post;
import com.Shashwat.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    public PostApiController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostPageResponse list(@RequestParam(defaultValue = "0") int page) {
        Page<Post> posts = postService.findAllOrderedByDatePageable(Math.max(0, page));
        return new PostPageResponse(
                posts.getContent().stream().map(this::toSummary).toList(),
                posts.getNumber(),
                posts.getTotalPages(),
                posts.getTotalElements(),
                posts.hasNext(),
                posts.hasPrevious());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> get(@PathVariable Long id) {
        return postService.findForId(id)
                .map(post -> ResponseEntity.ok(toDetail(post)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private PostSummary toSummary(Post post) {
        return new PostSummary(post.getId(), post.getTitle(), post.getBody(), date(post), author(post));
    }

    private PostDetailResponse toDetail(Post post) {
        List<CommentResponse> comments = post.getComments().stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getBody(), date(comment),
                        comment.getUser() == null ? "Unknown" : comment.getUser().getUsername()))
                .toList();
        return new PostDetailResponse(post.getId(), post.getTitle(), post.getBody(), date(post), author(post), comments);
    }

    private String author(Post post) {
        return post.getUser() == null ? "Unknown" : post.getUser().getUsername();
    }

    private String date(Post post) {
        return post.getCreateDate() == null ? null : post.getCreateDate().toInstant().toString();
    }

    private String date(Comment comment) {
        return comment.getCreateDate() == null ? null : comment.getCreateDate().toInstant().toString();
    }

    public record PostPageResponse(List<PostSummary> content, int page, int totalPages, long totalElements,
                                   boolean hasNext, boolean hasPrevious) { }

    public record PostSummary(Long id, String title, String body, String createDate, String author) { }

    public record PostDetailResponse(Long id, String title, String body, String createDate, String author,
                                     List<CommentResponse> comments) { }

    public record CommentResponse(Long id, String body, String createDate, String author) { }
}

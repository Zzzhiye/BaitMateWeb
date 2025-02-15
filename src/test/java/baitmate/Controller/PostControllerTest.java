package baitmate.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import baitmate.DTO.PostDto;
import baitmate.DTO.CreateCommentDto;
import baitmate.Service.PostService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

  private MockMvc mockMvc;

  @Mock private PostService postService;

  @InjectMocks private PostController postController;

  private PostDto mockPost;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(postController).build();

    
    mockPost = new PostDto();
    mockPost.setId(1L);
    mockPost.setPostTitle("Test Title");
    mockPost.setPostContent("Test Content");
  }

  
  @Test
  void getAllPosts() {
    List<PostDto> posts = Arrays.asList(mockPost);
    when(postService.getAllPosts()).thenReturn(posts);

    List<PostDto> result = postController.getAllPosts();
    assertEquals(1, result.size());
    assertEquals("Test Title", result.get(0).getPostTitle());
  }

  
  @Test
  void getPostById_Success() {
    when(postService.getPostById(1L)).thenReturn(mockPost);

    ResponseEntity<PostDto> response = postController.getPostById(1L);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Test Title", response.getBody().getPostTitle());
  }

  
  @Test
  void deletePost_Success() {
    doNothing().when(postService).deletePost(1L, 1L);

    ResponseEntity<Void> response = postController.deletePost(1L, 1L);
    assertEquals(204, response.getStatusCodeValue());
  }

  
  @Test
  void deletePost_Fail() {
    doThrow(new RuntimeException()).when(postService).deletePost(1L, 1L);

    ResponseEntity<Void> response = postController.deletePost(1L, 1L);
    assertEquals(403, response.getStatusCodeValue());
  }
}

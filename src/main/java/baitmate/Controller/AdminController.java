package baitmate.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import baitmate.Repository.ImageRepository;
import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.Service.ImageService;
import baitmate.model.Image;
import baitmate.model.Post;
import baitmate.model.User;

@Controller
@RequestMapping("/admin/post")
public class AdminController {
	
	
	@Autowired
	PostRepository postRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ImageRepository imgRepo;
	
	@Autowired
	ImageService imageServiceimpl;
	
	
	@RequestMapping("")
	public String post(@RequestParam(defaultValue = "1") int id, @RequestParam(required=false) String status, Model model) {
	
		Page<Post> postList;
		if(status==null || status.equals("")) {
			Pageable pageable=PageRequest.of(id-1, 3,Sort.by("postStatus").descending());
			postList=postRepo.findAll(pageable);
			
		}else {
			Pageable pageable=PageRequest.of(id-1, 3,Sort.by("postTime").descending());
			System.out.println("aaa"+status);
			postList=postRepo.searchPostByFilter(status, pageable);
		}
		
		model.addAttribute("totalPages", postList.getTotalPages());
		model.addAttribute("postList", postList);
		model.addAttribute("currentPage", id);
		model.addAttribute("selectedStatus", status);
		return "Home";
	}
	
	@GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") Long imageId) {
        try {
        	System.out.println("into getImage method");
            byte[] imageData = imageServiceimpl.getImageByImageId(imageId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/jpeg"); // Change MIME type as necessary

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	@GetMapping("/verifyPost/{id}")
	public String userPost(@PathVariable int id, Model model) {
		Post post = postRepo.findById((long) id).orElseThrow(() -> new RuntimeException("Post not found"));
		System.out.println("into userPost method");
		model.addAttribute("post", post);
		
		System.out.println("image ID ");
		List<Image> imageIds = imageServiceimpl.getImageByPostId(post.getId());
		model.addAttribute("imageIds", imageIds);
		//System.out.println("aaa"+imageIds.get(0).getImage());
		List<Post> allPosts = post.getUser().getPosts();
		model.addAttribute("postTotal", allPosts.size());
		model.addAttribute("postRej", allPosts.stream().filter(p -> "banned".equalsIgnoreCase(p.getPostStatus())).count());
		
		return "Post";
	}
	
	@GetMapping("/user/userPost/{status}/{id}")
	public String postStatus(@PathVariable int id, @PathVariable String status) {
		//update post status
		Post post = postRepo.findById((long) id).orElseThrow(() -> new RuntimeException("Post not found"));
		if(post.getPostStatus().equals("pending") || post.getPostStatus().equals("petition")) {
			post.setPostStatus(status);
			postRepo.save(post);
		}
		return "redirect:/admin/post";
		
	}
	
	@GetMapping("/user/userAccount/{status}/{id}")
	public String userStatus(@PathVariable int id, @PathVariable String status) {
		//update user status
		User user=userRepo.searchByUserId(id);
		if(user.getUserStatus().equals("active")) {
			user.setUserStatus(status);
			userRepo.save(user);
		}
		return "redirect:/admin/post";
		
	}
	
	@GetMapping("/user/userPost/{id}")
	public String userPastpost(@PathVariable int id, Model model) {
		User u= userRepo.searchByUserId(id);
		model.addAttribute("postList", u.getPosts());
		return "PastPost";
	}

}

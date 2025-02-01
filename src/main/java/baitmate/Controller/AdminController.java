package baitmate.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.model.Post;
import baitmate.model.User;



@Controller
@RequestMapping("/admin/post")
public class AdminController {
	
	
	@Autowired
	PostRepository postRepo;
	
	@Autowired
	UserRepository userRepo;
	
	
	@RequestMapping("")
	public String post(Model model) {
		Pageable pageable=PageRequest.of(0, 3,Sort.by("postStatus").descending());
		Page<Post> postList=postRepo.searchPostByPostStatus("appeal",pageable);
		model.addAttribute("postList", postList);
		return "Home";
	}
	
	@RequestMapping("/{id}")
	public String post(@PathVariable int id, Model model) {
		Pageable pageable=PageRequest.of(id-1, 3,Sort.by("postStatus").descending());
		Page<Post> postList=postRepo.searchPostByPostStatus("appeal",pageable);
		model.addAttribute("totalPages", postList.getTotalPages());
		model.addAttribute("postList", postList);
		return "Home";
	}
	
	@RequestMapping("/user/{id}")
	public String userPost(@PathVariable int id, Model model) {
		Post post=postRepo.searchPostByPostId(id);
		
		
		model.addAttribute("post", post);
		
		List<Post> allPosts = post.getUser().getPosts();
		model.addAttribute("postTotal", allPosts.size());
		model.addAttribute("postRej", allPosts.stream().filter(p -> "banned".equalsIgnoreCase(p.getPostStatus())).count());
		return "Post";
	}
	
	@GetMapping("/user/userPost/{status}/{id}")
	public String postStatus(@PathVariable int id, @PathVariable String status) {
		Post post=postRepo.searchPostByPostId(id);
		if(post.getPostStatus().equals("pending") || post.getPostStatus().equals("petition")) {
			post.setPostStatus(status);
			postRepo.save(post);
		}
		return "redirect:/admin/post";
		
	}
	
	@GetMapping("/user/userAccount/{status}/{id}")
	public String userStatus(@PathVariable int id, @PathVariable String status) {
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

package baitmate.Controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import baitmate.model.*;
import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import baitmate.Repository.CatchRecordRepository;
import baitmate.Service.AdminService;
import baitmate.Service.ImageService;
import baitmate.Service.PostService;
import baitmate.Service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	AdminService adminServiceImpl;

	@Autowired
	PostService postServiceImpl;

	@Autowired
	UserService userServiceImpl;

	@Autowired
	ImageService imageServiceimpl;

	@Autowired
	CatchRecordRepository catchRecordRepository;

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("admin", new Admin());
		return "login";
	}

	@PostMapping("/validate/login")
	public String login(Admin admin, HttpSession sessionObj, Model model) {
		Admin dataU = adminServiceImpl.searchUserByUserName(admin.getUsername());
		if (dataU == null) {
			model.addAttribute("errorMsg", "Your user name or password are wrong, please try again!");
			model.addAttribute("user", new Admin());
			return "login";
		} else {
			if (dataU.getPassword().equals(admin.getPassword())) {
				sessionObj.setAttribute("username", dataU.getUsername());
				sessionObj.setAttribute("customerId", dataU.getUsername());
				return "redirect:/admin/home";
			} else {
				model.addAttribute("errorMsg", "Your user name or password are wrong, please try again!");
				model.addAttribute("user", new Admin());
				return "login";
			}
		}
	}

	@GetMapping("/admin/logout")
	public String logout(HttpSession sessionObj, Model model, SessionStatus sessionStatus) {
		sessionObj.invalidate();
		sessionStatus.setComplete();
		return "redirect:/login";
	}

	@GetMapping("/admin/home")
	public String home(HttpSession sessionObj, Model model) {
		return "homePage";
	}

	@GetMapping("/admin/post")
	public String post(@RequestParam(name = "id", defaultValue = "1") int id,
			@RequestParam(name = "status", required = false) String status, Model model) {
		Page<Post> postList;
		if (status == null || status.equals("")) {
			Pageable pageable = PageRequest.of(id - 1, 10, Sort.by("postStatus").descending());
			postList = postServiceImpl.findAll(pageable);

		} else {
			Pageable pageable = PageRequest.of(id - 1, 10, Sort.by("postTime").descending());
			postList = postServiceImpl.searchPostByFilter(status, pageable);
		}

		model.addAttribute("totalPages", postList.getTotalPages());
		model.addAttribute("postList", postList);
		model.addAttribute("currentPage", id);
		model.addAttribute("selectedStatus", status);
		return "postverification";
	}

	@GetMapping("/admin/post/image/{imageId}")
	public ResponseEntity<byte[]> getImage(@PathVariable("imageId") Long imageId) {
		try {
			byte[] imageData = imageServiceimpl.getImageByImageId(imageId);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "image/jpeg"); 

			return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/admin/post/verifyPost2")
	public String userPost(@RequestParam Long id, Model model, RedirectAttributes redirct) {
		return "redirect:/admin/post/verifyPage?id=" + id;
	}

	@GetMapping("/admin/post/verifyPage")
	public String userPost(Model model, @RequestParam(required = true) Long id) {
		Post post = postServiceImpl.findById(id);
		model.addAttribute("post", post);
		List<Image> imageIds = imageServiceimpl.getImageByPostId(post.getId());
		model.addAttribute("imageIds", imageIds);
		List<Post> allPosts = post.getUser().getPosts();
		model.addAttribute("postTotal", allPosts.size());
		model.addAttribute("postRej",
				allPosts.stream().filter(p -> "banned".equalsIgnoreCase(p.getPostStatus())).count());

		return "post";
	}

	@GetMapping("/admin/post/user/userPost/{status}/{id}")
	public String postStatus(@PathVariable int id, @PathVariable String status) {
		
		Post post = postServiceImpl.findById((long) id);
		if (post.getPostStatus().equals("pending") || post.getPostStatus().equals("petition")) {
			post.setPostStatus(status);
			postServiceImpl.save(post);
		}
		return "redirect:/admin/post";

	}

	@GetMapping("/admin/post/user/userAccount/{status}/{id}")
	public String userStatus(@PathVariable int id, @PathVariable String status) {
		
		User user = userServiceImpl.searchByUserId(id);
		if (user.getUserStatus().equals("active")) {
			user.setUserStatus(status);
			userServiceImpl.save(user);

			List<Post> posts = user.getPosts();
			posts.stream().forEach(p -> {
				if (p.getPostStatus().equals("pending") || p.getPostStatus().equals("petition")) {
					p.setPostStatus("banned");
					postServiceImpl.save(p);
				}
			});
		}
		return "redirect:/admin/post";

	}

	@PostMapping("/admin/post/user/userPost")
	public String userPastpost(@RequestParam int id, Model model) {
		User u = userServiceImpl.searchByUserId(id);

		List<Post> pastPostList = u.getPosts();

		for (Post p : pastPostList) {
			p.setImages(imageServiceimpl.getImageByPostId(p.getId()));
		}

		model.addAttribute("postList", pastPostList);

		return "pastpost";
	}

	@GetMapping("/admin/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("admin", new Admin());
		return "register";
	}

	@PostMapping("/admin/register")
	public String registerAdmin(@Valid @ModelAttribute("admin") Admin admin, BindingResult bindingResult,
			@RequestParam String confirmPassword, RedirectAttributes redirectAttributes, Model model) {

		
		Admin existingAdmin = adminServiceImpl.searchUserByUserName(admin.getUsername());
		if (existingAdmin != null) {
			model.addAttribute("errorMessage", "Username already exists");
			return "register";
		}

		
		if (!admin.getPassword().equals(confirmPassword)) {
			model.addAttribute("errorMessage", "Passwords do not match");
			return "register";
		}

		
		if (bindingResult.hasErrors()) {
			return "register";
		}

		try {
			
			adminServiceImpl.createAdmin(admin);
			redirectAttributes.addFlashAttribute("successMessage",
					"Admin account created successfully! You can now login.");
			return "redirect:/login";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Failed to create account: " + e.getMessage());
			return "register";
		}
	}

	@GetMapping("/api/dashboard-data")
	@ResponseBody
	public Map<String, Object> getDashboardData() {
		Map<String, Object> data = new HashMap<>();

		
		Map<Integer, Long> postActivity = postServiceImpl.getTodayPostActivity();

		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfDay = now.with(LocalTime.MIN);
		LocalDateTime endOfDay = now.with(LocalTime.MAX);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String startTime = startOfDay.format(formatter);
		String endTime = endOfDay.format(formatter);

		List<CatchRecord> todayCatches = catchRecordRepository.findByTimeBetween(startTime, endTime);
		Map<Integer, Long> catchActivity = new HashMap<>();
		for (CatchRecord c : todayCatches) {
			LocalDateTime recordTime = LocalDateTime.parse(c.getTime(), formatter);
			int hour = recordTime.getHour();
			catchActivity.merge(hour, 1L, Long::sum);
		}

		
		List<Long> postData = new ArrayList<>(24);
		List<Long> catchData = new ArrayList<>(24);

		for (int i = 0; i < 24; i++) {
			postData.add(postActivity.getOrDefault(i, 0L));
			catchData.add(catchActivity.getOrDefault(i, 0L));
		}

		data.put("postActivity", postData);
		data.put("catchActivity", catchData);

		return data;
	}
}

package baitmate.Controller;

import baitmate.Repository.RedDotRepository;
import baitmate.model.RedDot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import baitmate.DTO.RedDotResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/redDots")
public class RedDotController {
    @Autowired
    private RedDotRepository redDotRepository;

    @GetMapping("/{userId}")
    public List<RedDotResponse> getRedDots(@PathVariable Long userId) {
        List<RedDot> redDots = redDotRepository.findByReceiverIdOrderByTimeDesc(userId);
        return redDots.stream().map(RedDotResponse::new).collect(Collectors.toList());
    }
}

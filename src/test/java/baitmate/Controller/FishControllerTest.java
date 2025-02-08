package baitmate.Controller;

import baitmate.Repository.FishRepository;
import baitmate.config.SecurityConfig;
import baitmate.model.Fish;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FishController.class) // 仅加载 FishController
@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)  // 需要导入你的 Security 配置类
@AutoConfigureMockMvc(addFilters = false)
class FishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FishRepository fishRepository; // Mock 数据库

    /** ✅ 测试成功返回图片 */
    @Test
    void getFishImage_Success() throws Exception {
        // 模拟数据库中的 Fish 对象
        Fish fish = new Fish();
        fish.setId(1L);
        fish.setFishImage(new byte[]{1, 2, 3, 4}); // 假设的图片数据

        when(fishRepository.findById(1L)).thenReturn(Optional.of(fish)); // 当请求 id=1L 时，返回 Fish

        mockMvc.perform(get("/fish/image/1"))
                .andExpect(status().isOk()) // 期望 200 OK
                .andExpect(header().string("Content-Type", "image/jpeg")) // 期望 Content-Type
                .andExpect(content().bytes(new byte[]{1, 2, 3, 4})); // 期望返回的图片数据
    }

    /** ✅ 测试找不到 Fish */
    @Test
    void getFishImage_NotFound() throws Exception {
        when(fishRepository.findById(99L)).thenReturn(Optional.empty()); // 当 id=99L 时，返回空

        mockMvc.perform(get("/fish/image/99"))
                .andExpect(status().isNotFound()); // 期望 404
    }
}

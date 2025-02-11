package baitmate.config; // 根据你的项目包结构调整

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

  @Autowired private SessionCheckInterceptor sessionCheckInterceptor;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**") // 允许所有路径
        .allowedOriginPatterns("*") // 允许所有来源（Spring 6+ 推荐）
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
  }

  @Value("${file.upload.path}")
  private String uploadPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadPath + "/");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(sessionCheckInterceptor)
        .addPathPatterns("/admin/**")
        .excludePathPatterns("/uploads/**", "/api", "/login", "/css/**", "/js/**", "/images/**");
  }
}

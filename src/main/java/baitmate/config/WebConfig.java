package baitmate.config; // 根据你的项目包结构调整

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private SessionCheckInterceptor sessionCheckInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionCheckInterceptor)
		.addPathPatterns("/admin/**");
	}
	
}

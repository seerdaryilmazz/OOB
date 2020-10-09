package ekol.gateway;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kilimci on 22/11/2017.
 */
@Controller
public class HomeController {

	@GetMapping
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/home");
    }
    
    @ResponseBody
    @GetMapping("/login-success")
    public String loginSuccess(HttpServletResponse response) throws IOException {
    	return "<script>window.close();</script>";
    }

}

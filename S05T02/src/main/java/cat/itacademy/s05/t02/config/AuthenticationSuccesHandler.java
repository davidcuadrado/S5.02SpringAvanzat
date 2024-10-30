package cat.itacademy.s05.t02.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.HttpServletRequest;
import org.springframework.security.web.authentication.HttpServletResponse;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ServletException;

public class AuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
		if(isAdmin) {
			setDefaultTargetUrl("/admin/home");
		} else {
			setDefaultTargetUrl("/user/home");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}

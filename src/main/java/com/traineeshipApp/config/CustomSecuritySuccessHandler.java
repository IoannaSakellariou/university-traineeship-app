package com.traineeshipApp.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.traineeshipApp.domainmodel.User;

@Configuration
public class CustomSecuritySuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	 @Override
	    protected void handle(
	    		HttpServletRequest request, 
	    		HttpServletResponse response, 
	    		Authentication authentication)
	    throws java.io.IOException {
	        String targetUrl = determineTargetUrl(authentication);
	        if(response.isCommitted()) return;
	        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	        redirectStrategy.sendRedirect(request, response, targetUrl);
	    }

	    protected String determineTargetUrl(Authentication authentication){
	        String url = "/login?error=true";
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        List<String> roles = new ArrayList<String>();
	        
	        for(GrantedAuthority a : authorities){
	            roles.add(a.getAuthority());
	        }
	        if(roles.contains("STUDENT")) {
	            url = "/student/dashboard";
	        } 
	        else if(roles.contains("PROFESSOR")) {
	            url = "/professor/dashboard";
	        } 
	        else if(roles.contains("COMPANY")) {
	            url = "/company/dashboard";
	        } 
	        else if(roles.contains("COMMITTEE_MEMBER")) {
	            url = "/committeemember/dashboard";
	        }
	        
	        return url;
	    }
	    
	   
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	                                        Authentication authentication) throws IOException {
	        User user = (User) authentication.getPrincipal();
	        
	        if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("PROFESSOR"))) {
	            response.sendRedirect("/professor/dashboard");
	            
	        } else if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("STUDENT"))) {
	            response.sendRedirect("/student/dashboard");
	            
	        } else if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("COMPANY"))) {
	            response.sendRedirect("/company/dashboard");
	            
	        } else if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER"))) {
	            response.sendRedirect("/user/dashboard");
	        }else if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("COMMITTEE_MEMBER"))) {
	            response.sendRedirect("/committeemember/dashboard");
	        }
	    }
	    
}

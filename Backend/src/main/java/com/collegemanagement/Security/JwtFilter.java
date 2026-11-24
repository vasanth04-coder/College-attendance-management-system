package com.collegemanagement.Security;
import com.collegemanagement.Exception.InactiveAccException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.UserRepo;
import com.collegemanagement.Service.JwtService;
import com.collegemanagement.Service.MyUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    @Autowired
    JwtService jwtService;
    MyUserDetailsService myUserDetailsService;
    UserRepo userRepo;

    public JwtFilter(JwtService jwtService,MyUserDetailsService myUserDetailsService,UserRepo userRepo)
    {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
      String head =  request.getHeader("Authorization");

      if(head!= null && head.startsWith("Bearer "))
      {
        String token = head.substring(7);

        try
        {
            String email = jwtService.extractUser(token);

            User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found"));

            if(user.getStatus()!= Status.ACTIVE)
            {
                throw new InactiveAccException("Your Account is InActive");
            }

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        catch (JwtException e)
        {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("plain/text");
              response.getWriter().write(
                      "Invalid Or Expired Token"
              );
              return;
        }
      }
      filterChain.doFilter(request,response);
    }
}

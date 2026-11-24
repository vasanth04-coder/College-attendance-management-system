package com.collegemanagement.Service;
import com.collegemanagement.Exception.InactiveAccException;
import com.collegemanagement.Exception.UserNotFoundException;
import com.collegemanagement.Model.Enum.Status;
import com.collegemanagement.Model.User;
import com.collegemanagement.Repository.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hibernate.id.enhanced.CustomOptimizerDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService
{
    @Value("${jwt.secret}")
    String secret;

    @Autowired
    UserRepo userRepo;

    public JwtService(UserRepo userRepo)
    {
        this.userRepo = userRepo;
    }


    public String generateToken(Authentication authentication)
    {
        SecretKey key =  Keys.hmacShaKeyFor(secret.getBytes());

        String role = authentication.getAuthorities()
                .stream()
                .map(auth->auth.getAuthority())
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("User has no Authority"));

                System.out.println("Generate Token");


                String email = authentication.getName();

                User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("User Not Found"));

                if(user.getStatus()!= Status.ACTIVE)
                {
                    throw new InactiveAccException("Your Account is InActive..");
                }

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("role",role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(key)
                .compact();
    }

    public String extractUser(String token)
    {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)//
                .getPayload()
                .getSubject();
    }
}
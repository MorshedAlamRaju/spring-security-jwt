package bd.edu.seu.springsecurityjwt;

import bd.edu.seu.springsecurityjwt.models.AuthenticationRequest;
import bd.edu.seu.springsecurityjwt.models.AuthenticationResponse;
import bd.edu.seu.springsecurityjwt.services.MyUserDetailsService;
import bd.edu.seu.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
public class HelloResource {

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private MyUserDetailsService userDetailsService;

   @Autowired
   private JwtUtil jwtTokenUtil;

   @RequestMapping(value = "/hello")
   public String hello(){
      return "<h1>hello world</h1>";
   }

   @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
   public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
      throws Exception {
      try{
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                 authenticationRequest.getUsername(),
                 authenticationRequest.getPassword()
         ));
      } catch(BadCredentialsException e){
         throw new Exception("Incorrect username or Password", e);
      }

      final UserDetails userDetails = userDetailsService
              .loadUserByUsername(authenticationRequest.getUsername());

      final String jwt = jwtTokenUtil.generateToken(userDetails);

      return ResponseEntity.ok(new AuthenticationResponse(jwt));

   }

}

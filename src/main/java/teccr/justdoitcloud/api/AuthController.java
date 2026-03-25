package teccr.justdoitcloud.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teccr.justdoitcloud.security.JwtUtil;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public static record LoginRequest(String username, String password) {}
    public static record AuthResponse(String token, String tokenType, String expiresAt) {}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        var principal = auth.getPrincipal();
        // recuperar roles desde authorities
        @SuppressWarnings("unchecked")
        List<String> roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(auth.getName(), roles);
        // calcular expiry (se usa la propiedad en JwtUtil)
        var claims = jwtUtil.validateAndParse(token).getBody();
        String expIso = Instant.ofEpochMilli(claims.getExpiration().getTime())
                .atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", expIso));
    }
}
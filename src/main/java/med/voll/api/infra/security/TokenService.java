package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;



@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecurity;

    //metoco para generar mi token
    public String generarToken(Usuario usuario){
       //este codigo se copio de jwt.io AuthO se modificaronm
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecurity);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    // aqui esta siendo geverado el subject
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaDeExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException("error al generar token jwt", exception);
        }
    }

    //metodo para obtener el subject/usuario de mi token
    public String getSubject(String token){
        if (token == null) {
            throw new RuntimeException("token es mulo");
        }
        // copiamos codigo de jwt.io AuthO
        DecodedJWT verifier = null;
        try {
            var algorithm = Algorithm.HMAC256(apiSecurity);
            verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("API Voll.med")
                    // reusable verifier instance
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            throw new RuntimeException("token JWT invalido o expirado");
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }

    private Instant generarFechaDeExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}

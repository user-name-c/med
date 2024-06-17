package med.voll.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//programacion orientada a aspectos
@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }

    //error datos duplicados
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity tratarError400(DataIntegrityViolationException e){
        var error = e.getMessage();
        return ResponseEntity.badRequest().body(error);
    }

    //mendaje para campos nulos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream()
                .map(DatosErrorValidacion::new)
                .toList();
        return ResponseEntity.badRequest().body(errores);
    }

    //dto para mensaje de error 400
    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error){
           this(error.getField(), error.getDefaultMessage());
        }
    }
}

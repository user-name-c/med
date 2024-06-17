package med.voll.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.direccion.DatosDireccion;


public record MedicoActualizarDTO(
        @NotNull
        Long id,

        String nombre,

        String documento,

        DatosDireccion direccion
){
}

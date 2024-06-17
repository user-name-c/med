package med.voll.api.domain.medico;

public record MedicoDTO (
        Long id,
         String nombre,
         String email,
         String documento,
         String especialidad
){
    public MedicoDTO (Medico medico) {
        this(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getDocumento(),
                medico.getEspecialidad().toString()
        );
    }
}

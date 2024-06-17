package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Boolean activo;
    private String nombre;
    private String email;
    private String telefono;
    private String documento;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    //esta clase
    @Embedded
    private Direccion direccion;

    public Medico(DatosRegistoMedico datosRegistoMedico) {
        this.activo = true;
        this. nombre = datosRegistoMedico.nombre();
       this.email = datosRegistoMedico.email();
       this.telefono = datosRegistoMedico.telefono();
       this.documento = datosRegistoMedico.documento();
        this.especialidad = datosRegistoMedico.especialidad();
        this.direccion = new Direccion(datosRegistoMedico.direccion());
    }


    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void actuallizarDatos(MedicoActualizarDTO medicoActualizarDTO) {
        if(medicoActualizarDTO.nombre() != null) {
            this.nombre = medicoActualizarDTO.nombre();
        }
        if(medicoActualizarDTO.documento() != null) {
            this.documento = medicoActualizarDTO.documento();
        }
        if(medicoActualizarDTO.direccion() != null) {
            this.direccion = direccion.actualizarDatos(medicoActualizarDTO.direccion());
        }
    }

    public void desactivarMedico() {
        this.activo = false;
    }
}

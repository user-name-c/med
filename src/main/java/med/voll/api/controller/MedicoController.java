package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<MedicoDTO> registrarMedico(@RequestBody @Valid DatosRegistoMedico datosRegistoMedico,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        System.out.println(datosRegistoMedico);
        Medico medico = medicoRepository.save(new Medico(datosRegistoMedico));
        MedicoDTO medicoDTO = new MedicoDTO(medico);

        // Return 201 created
        // URL donde encontrar al medico
        // Get http://localhost:8080/medicos/xx
        // uri component builder nos ayuda a obtener los datos del servidor
        URI url = uriComponentsBuilder.path("/medicos/{id}")
                .buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(medicoDTO);


    }

    ////Ejemplo de mapeado usando stream
//    @GetMapping
//    public List<MedicoDTO> listadoMedicos(){
//        return medicoRepository.findAll().stream()
//                .map(MedicoDTO::new)
//                .toList();
//    }

//Ejemplo usando pageable nos permite ordenar los datos en el front desde la uri http://localhost:8080/medicos?size=1&page=1
    @GetMapping
    public ResponseEntity<Page<MedicoDTO>>  listadoMedicos(@PageableDefault(size = 1) Pageable paginacion){
// regresa los medicos de la base de datos
        //        return medicoRepository.findAll(paginacion)
//                .map(MedicoDTO::new);
        // regresa los medicos activos
        //responseEhtity personaliza los retornos como encabezados
                return ResponseEntity.ok(medicoRepository
                        .findByActivoTrue(paginacion)
                        .map(MedicoDTO::new));

    }

    @PutMapping
    @Transactional //para mapear la clase medico a la DB
    public ResponseEntity actualizarMedico(@RequestBody @Valid MedicoActualizarDTO medicoActualizarDTO){
        Medico medico = medicoRepository.getReferenceById(medicoActualizarDTO.id());
        medico.actuallizarDatos(medicoActualizarDTO);
        return ResponseEntity.ok(new MedicoDTO(medico));
    }

    // el siguiente metodo borra directamente en la base de datos
//    @DeleteMapping("/{id}")
//    @Transactional
//    public void eliminarMedico(@PathVariable Long id){
//        Medico medico = medicoRepository.getReferenceById(id);
//        medicoRepository.delete(medico);
//    }

    // delete logico ahora solo desactivamos el medico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> retornaDatosMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new MedicoDTO(medico));
    }
}

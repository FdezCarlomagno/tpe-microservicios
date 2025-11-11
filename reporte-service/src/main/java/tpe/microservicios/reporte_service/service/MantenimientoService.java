package tpe.microservicios.reporte_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.reporte_service.entity.Mantenimiento;
import tpe.microservicios.reporte_service.repository.MantenimientoRespository;

import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoService {

    @Autowired
    private MantenimientoRespository  mantenimientoRespository;

    public Mantenimiento createMantenimiento(Mantenimiento mantenimiento) {
        return mantenimientoRespository.save(mantenimiento);
    }

    public void deleteMantenimiento(Integer id) {
        mantenimientoRespository.deleteById(id);
    }

    public Mantenimiento updateMantenimiento(Integer id, Mantenimiento mantenimiento) {
        Optional<Mantenimiento> existeMantenimiento = mantenimientoRespository.findById(id);
        if(existeMantenimiento.isPresent()) {
            Mantenimiento mantenimientoUpdate = existeMantenimiento.get();
            mantenimientoUpdate.setEstado(mantenimiento.getEstado());
            mantenimientoUpdate.setObservaciones(mantenimiento.getObservaciones());
            mantenimientoUpdate.setFechaMantenimiento(mantenimiento.getFechaMantenimiento());
            return mantenimientoRespository.save(mantenimientoUpdate);
        }
        return null;
    }
    public Mantenimiento findMantenimientoById(Integer id) {
        return mantenimientoRespository.findById(id).get();
    }
    public List<Mantenimiento> getAllMantenimientos() {
        return mantenimientoRespository.findAll();
    }

}

package tpe.microservicios.reporte_service.service;


import org.springframework.stereotype.Service;
import tpe.microservicios.reporte_service.clients.ViajesClient;
import tpe.microservicios.reporte_service.dto.ReporteMonopatinesUsadosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ViajesClient viajesClient;

    public List<ReporteMonopatinesUsadosDTO> getMonopatinUsado(){
        List<ReporteMonopatinesUsadosDTO> reportUsadosMonopatin = viajesClient.getMonopatinesUsados();
        return reportUsadosMonopatin;
    }
}

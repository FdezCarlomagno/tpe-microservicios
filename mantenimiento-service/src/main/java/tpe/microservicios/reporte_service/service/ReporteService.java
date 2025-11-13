package tpe.microservicios.reporte_service.service;


import org.springframework.stereotype.Service;
import tpe.microservicios.reporte_service.clients.ParadaClient;
import tpe.microservicios.reporte_service.dto.ReporteMonopatinesUsadosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ParadaClient paradaClient;

    public List<ReporteMonopatinesUsadosDTO> getMonopatinUsado(){
        List<ReporteMonopatinesUsadosDTO> reportUsadosMonopatin = paradaClient.getMonopatinesUsados();
        return reportUsadosMonopatin;
    }
}

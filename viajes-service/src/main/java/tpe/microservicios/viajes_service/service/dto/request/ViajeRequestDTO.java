package tpe.microservicios.viajes_service.service.dto.request;

public record ViajeRequestDTO(
         Long idMonopatin,
         Long idUserAccount,
         Long idParadaOrigen
         /**
          *   Se setearian al finalizar el viaje?
          *   Long idParadaDestino,
          *   float kilometros,
          *   float costoViaje
          */
) {
}

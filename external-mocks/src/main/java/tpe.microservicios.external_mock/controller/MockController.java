package tpe.microservicios.external_mock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.external_mock.service.MockService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/external")
public class MockController {

    private final MockService service;

    public MockController(MockService service) {
        this.service = service;
        this.service.seedDefault(); // seed inicial
    }

    // Crear topup (simula una orden de pago)
    @PostMapping("/mercadopago/topup")
    public ResponseEntity<Map<String, Object>> createTopup(@RequestBody Map<String, Object> req) {
        String accountId = (String) req.get("accountId");
        double amount = Double.parseDouble(req.get("amount").toString());
        String paymentId = service.createPayment(accountId, amount);
        return ResponseEntity.ok(Map.of("paymentId", paymentId, "status", "PENDING"));
    }

    // Estado de pago
    @GetMapping("/mercadopago/status/{paymentId}")
    public ResponseEntity<Map<String, Object>> paymentStatus(@PathVariable String paymentId) {
        Map<String, Object> p = service.getPayment(paymentId);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    // Confirmar pago (útil para pruebas)
    @PostMapping("/mercadopago/confirm/{paymentId}")
    public ResponseEntity<Void> confirm(@PathVariable String paymentId) {
        service.confirmPayment(paymentId);
        return ResponseEntity.ok().build();
    }

    // Monopatines cercanos (mock)
    @GetMapping("/maps/nearby")
    public ResponseEntity<Collection<Map<String, Object>>> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false, defaultValue = "1.0") double radiusKm) {
        return ResponseEntity.ok(service.getNearby(lat, lng, radiusKm));
    }

    // Reverse geocode simple
    @GetMapping("/maps/geocode")
    public ResponseEntity<Map<String, String>> geocode(@RequestParam double lat, @RequestParam double lng) {
        return ResponseEntity.ok(Map.of("address", "Parada Mock - Centro"));
    }
}

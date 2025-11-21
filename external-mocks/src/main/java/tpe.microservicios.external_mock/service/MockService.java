package tpe.microservicios.external_mock.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockService {

    private final Map<String, Map<String, Object>> payments = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> scooters = new ConcurrentHashMap<>();

    public String createPayment(String accountId, double amount) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> p = new HashMap<>();
        p.put("paymentId", id);
        p.put("accountId", accountId);
        p.put("amount", amount);
        p.put("status", "PENDING");
        p.put("createdAt", new Date());
        payments.put(id, p);
        return id;
    }

    public Map<String, Object> getPayment(String id) {
        return payments.get(id);
    }

    public void confirmPayment(String id) {
        Map<String, Object> p = payments.get(id);
        if (p != null) p.put("status", "SUCCESS");
    }

    public void seedDefault() {
        scooters.put("scoot-1", Map.of("id", "scoot-1", "lat", -34.6037, "lng", -58.3816, "status", "AVAILABLE"));
        scooters.put("scoot-2", Map.of("id", "scoot-2", "lat", -34.6040, "lng", -58.3820, "status", "MAINTENANCE"));
    }

    public Collection<Map<String, Object>> getNearby(double lat, double lng, double radiusKm) {
        // implementación simple: devolvemos todos los scooters seeds
        return scooters.values();
    }
}

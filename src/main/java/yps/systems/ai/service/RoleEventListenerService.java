package yps.systems.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import yps.systems.ai.model.Role;
import yps.systems.ai.repository.IRoleRepository;

import java.util.Optional;

@Service
public class RoleEventListenerService {

    private final IRoleRepository roleRepository;

    @Autowired
    public RoleEventListenerService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @KafkaListener(topics = "${env.kafka.topicEvent}")
    public void listen(@Payload String payload, @Header("eventType") String eventType, @Header("source") String source) {
        System.out.println("Processing " + eventType + " event from " + source);
        switch (eventType) {
            case "CREATE_ROLE":
                try {
                    Role role = new ObjectMapper().readValue(payload, Role.class);
                    roleRepository.save(role);
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "UPDATE_ROLE":
                try {
                    Role role = new ObjectMapper().readValue(payload, Role.class);
                    Optional<Role> roleOptional = roleRepository.findById(role.getId());
                    roleOptional.ifPresent(existingRole -> roleRepository.save(role));
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "DELETE_ROLE":
                Optional<Role> roleOptional = roleRepository.findById(payload.replaceAll("\"", ""));
                roleOptional.ifPresent(value -> roleRepository.deleteById(value.getId()));
                break;
            default:
                System.out.println("Unknown event type: " + eventType);
        }
    }

}

package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yps.systems.ai.model.Role;
import yps.systems.ai.repository.IRoleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/query/roleService")
public class RoleQueryController {

    private final IRoleRepository roleRepository;

    @Autowired
    public RoleQueryController(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getPersons() {
        List<Role>roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getPersonById(@PathVariable String id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

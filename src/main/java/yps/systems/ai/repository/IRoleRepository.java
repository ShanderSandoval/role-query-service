package yps.systems.ai.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Role;

@Repository
public interface IRoleRepository extends MongoRepository<Role, String> {
}

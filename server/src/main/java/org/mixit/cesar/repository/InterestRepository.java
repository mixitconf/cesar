package org.mixit.cesar.repository;

import org.mixit.cesar.model.member.Interest;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Interest}
 */
public interface InterestRepository extends CrudRepository<Interest, Long> {

    Interest findByName(String name);
}

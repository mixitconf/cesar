package org.mixit.cesar.repository;

import org.mixit.cesar.model.Member;
import org.springframework.data.repository.CrudRepository;

/**
 * {@link Member}
 */
public interface MemberRepository extends CrudRepository<Member, Long> {

}

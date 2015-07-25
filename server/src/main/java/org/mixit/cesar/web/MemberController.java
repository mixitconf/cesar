package org.mixit.cesar.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.mixit.cesar.model.Member;
import org.mixit.cesar.model.MemberResource;
import org.mixit.cesar.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    @RequestMapping
    public ResponseEntity<List<MemberResource>> getAll(){
        return new ResponseEntity<>(
                StreamSupport
                        .stream(memberRepository.findAll().spliterator(),false)
                        .map(m -> MemberResource.convert(m))
                        .collect(Collectors.toList()), HttpStatus.OK);
    }
}
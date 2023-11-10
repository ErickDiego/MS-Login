package com.example.demo.repository;

import com.example.demo.entity.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  UsuarioRepository extends MongoRepository<UsuarioEntity, String> {
}

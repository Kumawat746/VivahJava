package com.vivah.vivah.Repository;

import org.springframework.data.repository.CrudRepository;

import com.vivah.vivah.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {
}

package com.sikorski.commission.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();
}
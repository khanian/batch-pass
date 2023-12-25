package com.example.pass.repository.pass;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

// ReportingPolicy.IGNORE: 일치하지 않은 필드를 무시
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassModelMapper {
    PassModelMapper INSTANCE = Mappers.getMapper(PassModelMapper.class);

    // 필드명이 같지 않거나 custom하게 매핑하기 위해 @Mappinng을 추가
    @Mapping(target = "status", qualifiedByName = "defaultStatus")
    @Mapping(target = "remainingCount", source = "bulkPassEntity.count")
    PassEntity toPassEntity(BulkPassEntity bulkPassEntity, String userId);

    // BulkPassStatus와 관계없이 PassStatus 값을 설정
    @Named("defaultStatus")
    default PassStatus status(BulkPassStatus status) { return PassStatus.READY; }
}

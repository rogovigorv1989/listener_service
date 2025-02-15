package ru.t1.java.demo.listener_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.listener_service.model.Transaction;
import ru.t1.java.demo.listener_service.model.dto.TransactionDTO;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    Transaction toEntity(TransactionDTO transactionDTO);

    TransactionDTO toDto(Transaction transaction);
}

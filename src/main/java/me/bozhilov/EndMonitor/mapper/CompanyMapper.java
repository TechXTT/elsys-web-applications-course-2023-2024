package me.bozhilov.EndMonitor.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import me.bozhilov.EndMonitor.controller.resources.CompanyResource;
import me.bozhilov.EndMonitor.model.Company;

@Mapper(uses = { UserMapper.class, APIMapper.class })
public interface CompanyMapper {

    CompanyMapper COMPANY_MAPPER = Mappers.getMapper(CompanyMapper.class);

    Company fromCompanyResource(CompanyResource companyResource);

    CompanyResource toCompanyResource(Company company);

    List<CompanyResource> toCompanyResourceList(List<Company> companies);
}

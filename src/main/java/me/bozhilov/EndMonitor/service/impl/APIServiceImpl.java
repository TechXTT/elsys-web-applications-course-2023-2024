package me.bozhilov.EndMonitor.service.impl;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.bozhilov.EndMonitor.controller.resources.APIResource;
import me.bozhilov.EndMonitor.model.API;
import me.bozhilov.EndMonitor.repository.APIRepository;
import me.bozhilov.EndMonitor.service.APIService;
import me.bozhilov.EndMonitor.service.CompanyService;

import static me.bozhilov.EndMonitor.mapper.APIMapper.apiMapper;
import static me.bozhilov.EndMonitor.mapper.CompanyMapper.COMPANY_MAPPER;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class APIServiceImpl implements APIService {

    private final APIRepository apiRepository;
    private final CompanyService companyService;

    @Override
    public List<APIResource> findAll() {
        return apiMapper.toAPIResourceList(apiRepository.findAll());
    }

    @Override
    public Optional<APIResource> findById(Long id) {
        return Optional.ofNullable(apiMapper.toAPIResource(apiRepository.findById(id).orElse(null)));
    }

    @Override
    public API save(APIResource apiResource) {
        API api = apiMapper.fromAPIResource(apiResource);
        companyService.findByName(api.getCompany().getName())
                .ifPresentOrElse(
                        company -> api.setCompany(COMPANY_MAPPER.fromCompanyResource(company)),
                        () -> {
                            throw new EntityNotFoundException(
                                    "Company with name " + api.getCompany().getName() + " not found");
                        });
        return apiRepository.save(api);
    }

    @Override
    public API update(APIResource apiResource, Long id) {
        API api = apiMapper.fromAPIResource(apiResource);
        API apiToUpdate = apiRepository.findById(id).orElse(null);
        if (apiToUpdate == null) {
            return null;
        }
        if (api.getRoute() != null) {
            apiToUpdate.setRoute(api.getRoute());
        }
        if (api.getDescription() != null) {
            apiToUpdate.setDescription(api.getDescription());
        }
        if (api.getCompany() != null) {
            companyService.findByName(api.getCompany().getName())
                    .ifPresentOrElse(
                            company -> apiToUpdate.setCompany(COMPANY_MAPPER.fromCompanyResource(company)),
                            () -> {
                                throw new EntityNotFoundException(
                                        "Company with name " + api.getCompany().getName() + " not found");
                            });
        }
        return apiRepository.save(apiToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        apiRepository.deleteById(id);
    }

}

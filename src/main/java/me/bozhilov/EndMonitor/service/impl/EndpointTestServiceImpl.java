package me.bozhilov.EndMonitor.service.impl;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.bozhilov.EndMonitor.controller.resources.EndpointTestResource;
import me.bozhilov.EndMonitor.model.EndpointTest;
import me.bozhilov.EndMonitor.repository.EndpointTestRepository;
import me.bozhilov.EndMonitor.service.EndpointService;
import me.bozhilov.EndMonitor.service.EndpointTestService;

import static me.bozhilov.EndMonitor.mapper.EndpointTestMapper.endpointTestMapper;
import static me.bozhilov.EndMonitor.mapper.EndpointMapper.endpointMapper;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EndpointTestServiceImpl implements EndpointTestService {

    private final EndpointTestRepository endpointTestRepository;
    private final EndpointService endpointService;

    @Override
    public List<EndpointTestResource> findAll() {
        return endpointTestMapper.toEndpointTestResourceList(endpointTestRepository.findAll());
    }

    @Override
    public Optional<EndpointTestResource> findById(Long id) {
        return Optional.ofNullable(
                endpointTestMapper.toEndpointTestResource(endpointTestRepository.findById(id).orElse(null)));
    }

    @Override
    public EndpointTest save(EndpointTestResource endpointTestResource) {
        EndpointTest endpointTest = endpointTestMapper.fromEndpointTestResource(endpointTestResource);
        endpointService.findById(endpointTest.getEndpoint().getId())
                .ifPresentOrElse(
                        endpoint -> endpointTest.setEndpoint(endpointMapper.fromEndpointResource(endpoint)),
                        () -> {
                            throw new EntityNotFoundException(
                                    "Endpoint with id " + endpointTest.getEndpoint().getId() + " not found");
                        });
        return endpointTestRepository.save(endpointTest);
    }

    @Override
    public EndpointTest update(EndpointTestResource endpointTestResource, Long id) {
        EndpointTest endpointTest = endpointTestMapper.fromEndpointTestResource(endpointTestResource);
        EndpointTest endpointTestToUpdate = endpointTestRepository.findById(id).orElse(null);
        if (endpointTestToUpdate == null) {
            return null;
        }
        if (endpointTest.getDescription() != null) {
            endpointTestToUpdate.setDescription(endpointTest.getDescription());
        }
        if (endpointTest.getRequestBody() != null) {
            endpointTestToUpdate.setRequestBody(endpointTest.getRequestBody());
        }
        if (endpointTest.getRequestHeaders() != null) {
            endpointTestToUpdate.setRequestHeaders(endpointTest.getRequestHeaders());
        }
        if (endpointTest.getRequestParams() != null) {
            endpointTestToUpdate.setRequestParams(endpointTest.getRequestParams());
        }
        if (endpointTest.getResponseBody() != null) {
            endpointTestToUpdate.setResponseBody(endpointTest.getResponseBody());
        }
        if (endpointTest.getResponseHeaders() != null) {
            endpointTestToUpdate.setResponseHeaders(endpointTest.getResponseHeaders());
        }
        if (endpointTest.getResponseStatusCode() != null) {
            endpointTestToUpdate.setResponseStatusCode(endpointTest.getResponseStatusCode());
        }
        if (endpointTest.getEndpoint() != null) {
            endpointService.findById(endpointTest.getEndpoint().getId())
                    .ifPresentOrElse(
                            endpoint -> endpointTestToUpdate.setEndpoint(endpointMapper.fromEndpointResource(endpoint)),
                            () -> {
                                throw new EntityNotFoundException(
                                        "Endpoint with id " + endpointTest.getEndpoint().getId() + " not found");
                            });
        }
        return endpointTestRepository.save(endpointTestToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        endpointTestRepository.deleteById(id);
    }
}

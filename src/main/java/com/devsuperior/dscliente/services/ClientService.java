package com.devsuperior.dscliente.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscliente.dto.ClientDTO;
import com.devsuperior.dscliente.entities.Client;
import com.devsuperior.dscliente.repositories.ClientRepository;
import com.devsuperior.dscliente.services.exceptions.DatabaseException;
import com.devsuperior.dscliente.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	
	@Autowired
	private ClientRepository clientRepository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = clientRepository.findAll(pageRequest);
		Page<ClientDTO> result = list.map(x -> new ClientDTO(x));
		return result;
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		
		Optional<Client> obj = clientRepository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("404 - not found"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client(dto);
		entity = clientRepository.save(entity);
		return new ClientDTO(entity);
	}
	
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client client = clientRepository.getOne(id);
			client.setBirthDate(dto.getBirthDate());
			client.setChildren(dto.getChildren());
			client.setCpf(dto.getCpf());
			client.setIncome(dto.getIncome());
			client.setName(dto.getName());
			client = clientRepository.save(client);
			return new ClientDTO(client);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found "+id);
		}
	}
	
	public void delete(Long id) {
		try {
			clientRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id "+id+" not exists");
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("integrity violation "+e.getStackTrace().toString());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

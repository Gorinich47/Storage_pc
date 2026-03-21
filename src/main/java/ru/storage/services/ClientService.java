package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Client;
import ru.storage.repo.ClientRepository;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    ClientService(ClientRepository repository) {
        this.clientRepository = repository;
    }

    public List<Client> getAll() {
        return clientRepository.findAllByOrderByFirstNameAscLastNameAsc();
    }

    public void generateClients() {
        for (int i = 0; i < 10; i++) {
            Client randomClient = DemoService.FIO.randomClient();
            clientRepository.save(randomClient);
        }
    }
}

package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.storage.model.Client;
import ru.storage.repo.ClientRepository;
import ru.storage.utils.DemoUtils;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class ClientService extends BaseService<Client, ClientRepository> {

    private final ClientRepository clientRepository;

    @Autowired
    ClientService(ClientRepository clientRepository) {
        super(clientRepository);
        this.clientRepository = clientRepository;
    }

    @Override
    protected Client newEntity() {
        return new Client();
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAllByOrderByLastNameAscFirstNameAscPatronymicAscBirthDateAsc();
    }

    public List<Client> searchOrAll(String searchAll) {

        List<Client> clientList = List.of();
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            clientList = clientRepository.findBySearchIgnoreCase(searchAll);
        }
        return clientList;
    }

    public List<Client> searchOrAll(String searchAll, Limit limit) {

        List<Client> clientList = List.of();
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            clientList = clientRepository.findBySearchIgnoreCase(searchAll, limit);
        }
        return clientList;
    }

    public Page<Client> searchOrAll(Pageable pageable, String searchAll) {

        Page<Client> clientPage;
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            clientPage = clientRepository.findBySearchIgnoreCase(searchAll, pageable);
        } else {
            clientPage = getAll(pageable);
        }

        return clientPage;
    }

    public void generateClients() {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 1000; j++) {
                clients.add(DemoUtils.FIO.randomClient());
            }
            clientRepository.saveAll(clients);
            clients.clear();
        }
    }
}

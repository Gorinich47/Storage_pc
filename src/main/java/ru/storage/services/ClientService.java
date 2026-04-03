package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.storage.model.Client;
import ru.storage.repo.ClientRepository;
import ru.storage.utils.DemoUtils;

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
        return clientRepository.findAllByOrderByFirstNameAscLastNameAsc();
    }

    public List<Client> searchOrAll(String searchAll) {

        List<Client> clientList = List.of();
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            clientList = clientRepository.findBySearchIgnoreCase(searchAll);
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
        for (int i = 0; i < 10; i++) {
            Client randomClient = DemoUtils.FIO.randomClient();
            clientRepository.save(randomClient);
        }
    }
}

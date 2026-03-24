package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void generateClients() {
        for (int i = 0; i < 10; i++) {
            Client randomClient = DemoUtils.FIO.randomClient();
            clientRepository.save(randomClient);
        }
    }
}

package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.repo.AccountRepository;
import ru.storage.repo.BoxRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class BoxService extends BaseService<Box, BoxRepository> {

    private final AccountRepository accountRepository;
    private final BoxRepository boxRepository;

    @Autowired
    BoxService(AccountRepository accountRepository, BoxRepository boxRepository) {
        super(boxRepository);
        this.accountRepository = accountRepository;
        this.boxRepository = boxRepository;

    }

    @Override
    protected Box newEntity() {
        return new Box();
    }

    public Page<Box> searchOrAll(int page, int size, String searchAll) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("idBox").ascending());
        Page<Box> boxPage;
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            boxPage = boxRepository.findBySearchIgnoreCase(searchAll, pageable);
        } else {
            boxPage = boxRepository.findAllByOrderByIdBoxAsc(pageable);
        }

        return boxPage;
    }

    @Override
    public List<Box> getAll() {
        return boxRepository.findAllByOrderByIdBoxAsc();
    }

    public String toJson(List<Box> boxes) {
        List<Long> listIds = boxes.stream()
                .map(box -> box.getId())
                .toList();
        try {
            return listIds != null ? new ObjectMapper().writeValueAsString(listIds) : "[]";
        } catch (Exception e) {
            return "[]";
        }
    }

    public String toJson(Account account) {
        List<String> listIds = account.getBox()
                .stream()
                .map(box -> box.getId().toString())
                .toList();
        try {
            return listIds != null ? new ObjectMapper().writeValueAsString(listIds) : "[]";
        } catch (Exception e) {
            return "[]";
        }
    }

    public String toJson(Long accountId) {
        Account account = accountRepository.findById(accountId).get();
        if (account == null) {
            return "[]";
        } else {
            List<String> listIds = account.getBox()
                    .stream()
                    .map(box -> box.getId().toString())
                    .toList();
            try {
                return listIds != null ? new ObjectMapper().writeValueAsString(listIds) : "[]";
            } catch (Exception e) {
                return "[]";
            }
        }
    }

    public String getJsonId(Account account) {

        return (account.getBox() != null) ?
                new tools.jackson.databind
                        .ObjectMapper()
                        .writeValueAsString(account.getBox())
                : "[]";

    }


}


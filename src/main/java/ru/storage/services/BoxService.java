package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.repo.AccountRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class BoxService {

    //    List<Integer> getListId(List<Box> boxes){
//        return boxes.stream().map(box -> box.getId()).collect(Collectors.toList());
//    }
    @Autowired
    private AccountRepository accountRepository;

    public String toJson(List<Box> boxes) {
        List<Integer> listIds = boxes.stream().map(box -> box.getId()).toList();
        try {
            return listIds != null ? new ObjectMapper().writeValueAsString(listIds) : "[]";
        } catch (Exception e) {
            return "[]";
        }
    }

    public String toJson(Account account) {
        List<String> listIds = account.getBox().stream().map(box -> box.getId().toString()).toList();
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
            List<String> listIds = account.getBox().stream().map(box -> box.getId().toString()).toList();
            try {
                return listIds != null ? new ObjectMapper().writeValueAsString(listIds) : "[]";
            } catch (Exception e) {
                return "[]";
            }
        }
    }

    public String getJsonId(Account account) {

        return account.getBox() != null ? new tools.jackson.databind.ObjectMapper().writeValueAsString(account.getBox()) : "[]";

    }
}


package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.repo.AccountRepository;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class AccountService extends BaseService<Account, AccountRepository> {

    private final AccountRepository accountRepository;
    private final BoxService boxService;

    @Autowired
    AccountService(AccountRepository accountRepository, BoxService boxService) {
        super(accountRepository);
        this.boxService = boxService;
        this.accountRepository = accountRepository;
    }

    @Override
    protected Account newEntity() {
        return new Account();
    }

    public void save(Account account, List<Long> boxIds) {
        List<Box> boxList = boxService.getByListId(boxIds);
        account.setBox(boxList);
        accountRepository.save(account);
    }

    @Override
    public Account getByIdOrNew(Long id) {
        Account account = getById(id).orElse(new Account());
        if (account.getBox() == null) {
            account.setBox(List.of());
        }
        return account;
    }

}

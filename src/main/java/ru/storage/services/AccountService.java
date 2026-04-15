package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.model.Price;
import ru.storage.repo.AccountRepository;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class AccountService extends BaseService<Account, AccountRepository> {

    private final AccountRepository accountRepository;
    private final BoxService boxService;
    private final PriceService priceService;
    @Autowired
    AccountService(AccountRepository accountRepository, BoxService boxService, PriceService priceService) {
        super(accountRepository);
        this.boxService = boxService;
        this.accountRepository = accountRepository;
        this.priceService = priceService;
    }

    @Override
    protected Account newEntity() {
        return new Account();
    }

    public void save(Account account, Long[] boxIds) {
        if (boxIds != null) {
            List<Box> boxList = boxService.getByListId(List.of(boxIds));
            List<Price> priceList = priceService.getAllByBoxId(List.of(boxIds), account.getDateStart());
            account.setSumAmount(priceList.stream().mapToDouble(Price::getSumPrice).sum());
            account.setBox(boxList);
        } else {
            account.setBox(List.of());
        }
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

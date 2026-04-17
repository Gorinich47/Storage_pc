package ru.storage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.repo.AccountRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

            Double koeff = solveKoef(account.getDateStart(), account.getDateEnd());
            Double sumPrice = priceService.getTotalSum(List.of(boxIds), account.getDateStart());
            BigDecimal accountTotalCost = BigDecimal.valueOf(sumPrice * koeff);
            account.setSumAmount(accountTotalCost);
            account.setBox(boxList);
        } else {
            account.setBox(List.of());
        }
        accountRepository.save(account);
    }

    public Double solveKoef(Date start, Date end) {

        double koef = 0.0;

        LocalDate dateStart = start.toLocalDate();
        LocalDate dateEnd = end.toLocalDate();
        long months = ChronoUnit.MONTHS.between(dateStart, dateEnd);

        if (dateEnd.getMonthValue() == dateStart.getMonthValue()) { /* всё в пределах одного месяца */
            long days = ChronoUnit.DAYS.between(dateStart, dateEnd) + 1;
            koef = (double) days / dateEnd.lengthOfMonth(); // процент дней от целого месяца
        } else { /* разные месяцы: кол-во полных месяцев + остаток текущего + от начала месяца окончания */
            long daysStart = dateStart.lengthOfMonth() - dateStart.getDayOfMonth() + 1; // до конца месяца
            double koef1 = (double) daysStart / dateStart.lengthOfMonth();
            double koef2 = (double) dateEnd.getDayOfMonth() / dateEnd.lengthOfMonth();
            koef = koef1 + koef2 + months - 1;
        }

        return koef;
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

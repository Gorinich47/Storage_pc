package ru.storage.repo;

import ru.storage.model.Box;

import java.sql.Date;
import java.util.List;

public interface BoxRepositoryCustom {

    List<Box> findAllNew(Date date);
}

package com.epam.study.snet.model.dao;

import com.epam.study.snet.model.entity.Country;
import com.epam.study.snet.model.entity.User;
import com.epam.study.snet.controller.validators.ProfileValidator;

import java.util.List;

public interface UserDao {
    User create(ProfileValidator profile) throws DaoException;

    List<User> getList() throws DaoException;

    List<User> getList(User excludedUser) throws DaoException;

    List<User> getList(User excludedUser, Country country) throws DaoException;

    List<User> getList(User excludedUser, Country country, long skip, int limit) throws DaoException;

    List<User> getList(User excludedUser, long skip, int limit) throws DaoException;

    User getById(long id) throws DaoException;

    User getByUsername(String username) throws DaoException;

    long getNumber() throws DaoException;

    long getNumber(User excludedUser, Country country) throws DaoException;

    void update(User user) throws DaoException;

    void updateById(long id, ProfileValidator profile) throws DaoException;

    void removeById(long id) throws DaoException;

    List<Country> getCountries(User excludedUser) throws DaoException;
}
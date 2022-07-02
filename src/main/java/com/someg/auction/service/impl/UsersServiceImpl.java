package com.someg.auction.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.someg.auction.domain.Users;
import com.someg.auction.repository.UsersRepository;
import com.someg.auction.repository.search.UsersSearchRepository;
import com.someg.auction.service.UsersService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Users}.
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    private final UsersRepository usersRepository;

    private final UsersSearchRepository usersSearchRepository;

    public UsersServiceImpl(UsersRepository usersRepository, UsersSearchRepository usersSearchRepository) {
        this.usersRepository = usersRepository;
        this.usersSearchRepository = usersSearchRepository;
    }

    @Override
    public Users save(Users users) {
        log.debug("Request to save Users : {}", users);
        Users result = usersRepository.save(users);
        usersSearchRepository.save(result);
        return result;
    }

    @Override
    public Users update(Users users) {
        log.debug("Request to save Users : {}", users);
        Users result = usersRepository.save(users);
        usersSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Users> partialUpdate(Users users) {
        log.debug("Request to partially update Users : {}", users);

        return usersRepository
            .findById(users.getId())
            .map(existingUsers -> {
                if (users.getUsername() != null) {
                    existingUsers.setUsername(users.getUsername());
                }
                if (users.getName() != null) {
                    existingUsers.setName(users.getName());
                }
                if (users.getSurname() != null) {
                    existingUsers.setSurname(users.getSurname());
                }
                if (users.getTel() != null) {
                    existingUsers.setTel(users.getTel());
                }
                if (users.getEmail() != null) {
                    existingUsers.setEmail(users.getEmail());
                }
                if (users.getSsn() != null) {
                    existingUsers.setSsn(users.getSsn());
                }
                if (users.getStatus() != null) {
                    existingUsers.setStatus(users.getStatus());
                }

                return existingUsers;
            })
            .map(usersRepository::save)
            .map(savedUsers -> {
                usersSearchRepository.save(savedUsers);

                return savedUsers;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Users> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return usersRepository.findAll(pageable);
    }

    /**
     *  Get all the users where Id is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Users> findAllWhereIdIsNull() {
        log.debug("Request to get all users where Id is null");
        return StreamSupport
            .stream(usersRepository.findAll().spliterator(), false)
            .filter(users -> users.getId() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Users> findOne(Long id) {
        log.debug("Request to get Users : {}", id);
        return usersRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Users : {}", id);
        usersRepository.deleteById(id);
        usersSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Users> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);
        return usersSearchRepository.search(query, pageable);
    }
}

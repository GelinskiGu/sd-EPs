package com.gelinski.service;

import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DatabaseService {
    private final EntityManagerFactory emf;

    public void saveAccount(CreateAccountRequest request) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Account entity = new Account();
            entity.setName(request.getName());
            entity.setUser(request.getUser());
            entity.setPassword(request.getPassword());

            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            close();
        }
    }

    public Optional<Account> findByUser(String user) {
        try (EntityManager em = emf.createEntityManager()) {
            String jpql = "SELECT a FROM Account a WHERE a.user = :user";
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("user", user);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void close() {
        emf.close();
    }
}

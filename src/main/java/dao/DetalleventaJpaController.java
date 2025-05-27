/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.Detalleventa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Producto;
import dto.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Andrea Elena
 */
public class DetalleventaJpaController implements Serializable {

    public DetalleventaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detalleventa detalleventa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto codiProd = detalleventa.getCodiProd();
            if (codiProd != null) {
                codiProd = em.getReference(codiProd.getClass(), codiProd.getCodiProd());
                detalleventa.setCodiProd(codiProd);
            }
            Venta codiVent = detalleventa.getCodiVent();
            if (codiVent != null) {
                codiVent = em.getReference(codiVent.getClass(), codiVent.getCodiVent());
                detalleventa.setCodiVent(codiVent);
            }
            em.persist(detalleventa);
            if (codiProd != null) {
                codiProd.getDetalleventaCollection().add(detalleventa);
                codiProd = em.merge(codiProd);
            }
            if (codiVent != null) {
                codiVent.getDetalleventaCollection().add(detalleventa);
                codiVent = em.merge(codiVent);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detalleventa detalleventa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleventa persistentDetalleventa = em.find(Detalleventa.class, detalleventa.getIddetalle());
            Producto codiProdOld = persistentDetalleventa.getCodiProd();
            Producto codiProdNew = detalleventa.getCodiProd();
            Venta codiVentOld = persistentDetalleventa.getCodiVent();
            Venta codiVentNew = detalleventa.getCodiVent();
            if (codiProdNew != null) {
                codiProdNew = em.getReference(codiProdNew.getClass(), codiProdNew.getCodiProd());
                detalleventa.setCodiProd(codiProdNew);
            }
            if (codiVentNew != null) {
                codiVentNew = em.getReference(codiVentNew.getClass(), codiVentNew.getCodiVent());
                detalleventa.setCodiVent(codiVentNew);
            }
            detalleventa = em.merge(detalleventa);
            if (codiProdOld != null && !codiProdOld.equals(codiProdNew)) {
                codiProdOld.getDetalleventaCollection().remove(detalleventa);
                codiProdOld = em.merge(codiProdOld);
            }
            if (codiProdNew != null && !codiProdNew.equals(codiProdOld)) {
                codiProdNew.getDetalleventaCollection().add(detalleventa);
                codiProdNew = em.merge(codiProdNew);
            }
            if (codiVentOld != null && !codiVentOld.equals(codiVentNew)) {
                codiVentOld.getDetalleventaCollection().remove(detalleventa);
                codiVentOld = em.merge(codiVentOld);
            }
            if (codiVentNew != null && !codiVentNew.equals(codiVentOld)) {
                codiVentNew.getDetalleventaCollection().add(detalleventa);
                codiVentNew = em.merge(codiVentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleventa.getIddetalle();
                if (findDetalleventa(id) == null) {
                    throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detalleventa detalleventa;
            try {
                detalleventa = em.getReference(Detalleventa.class, id);
                detalleventa.getIddetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleventa with id " + id + " no longer exists.", enfe);
            }
            Producto codiProd = detalleventa.getCodiProd();
            if (codiProd != null) {
                codiProd.getDetalleventaCollection().remove(detalleventa);
                codiProd = em.merge(codiProd);
            }
            Venta codiVent = detalleventa.getCodiVent();
            if (codiVent != null) {
                codiVent.getDetalleventaCollection().remove(detalleventa);
                codiVent = em.merge(codiVent);
            }
            em.remove(detalleventa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detalleventa> findDetalleventaEntities() {
        return findDetalleventaEntities(true, -1, -1);
    }

    public List<Detalleventa> findDetalleventaEntities(int maxResults, int firstResult) {
        return findDetalleventaEntities(false, maxResults, firstResult);
    }

    private List<Detalleventa> findDetalleventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detalleventa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Detalleventa findDetalleventa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detalleventa> rt = cq.from(Detalleventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

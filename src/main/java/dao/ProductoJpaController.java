/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Detalleventa;
import java.util.ArrayList;
import java.util.Collection;
import dto.Detalle;
import dto.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Andrea Elena
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getDetalleventaCollection() == null) {
            producto.setDetalleventaCollection(new ArrayList<Detalleventa>());
        }
        if (producto.getDetalleCollection() == null) {
            producto.setDetalleCollection(new ArrayList<Detalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Detalleventa> attachedDetalleventaCollection = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaCollectionDetalleventaToAttach : producto.getDetalleventaCollection()) {
                detalleventaCollectionDetalleventaToAttach = em.getReference(detalleventaCollectionDetalleventaToAttach.getClass(), detalleventaCollectionDetalleventaToAttach.getIddetalle());
                attachedDetalleventaCollection.add(detalleventaCollectionDetalleventaToAttach);
            }
            producto.setDetalleventaCollection(attachedDetalleventaCollection);
            Collection<Detalle> attachedDetalleCollection = new ArrayList<Detalle>();
            for (Detalle detalleCollectionDetalleToAttach : producto.getDetalleCollection()) {
                detalleCollectionDetalleToAttach = em.getReference(detalleCollectionDetalleToAttach.getClass(), detalleCollectionDetalleToAttach.getCodiDeta());
                attachedDetalleCollection.add(detalleCollectionDetalleToAttach);
            }
            producto.setDetalleCollection(attachedDetalleCollection);
            em.persist(producto);
            for (Detalleventa detalleventaCollectionDetalleventa : producto.getDetalleventaCollection()) {
                Producto oldCodiProdOfDetalleventaCollectionDetalleventa = detalleventaCollectionDetalleventa.getCodiProd();
                detalleventaCollectionDetalleventa.setCodiProd(producto);
                detalleventaCollectionDetalleventa = em.merge(detalleventaCollectionDetalleventa);
                if (oldCodiProdOfDetalleventaCollectionDetalleventa != null) {
                    oldCodiProdOfDetalleventaCollectionDetalleventa.getDetalleventaCollection().remove(detalleventaCollectionDetalleventa);
                    oldCodiProdOfDetalleventaCollectionDetalleventa = em.merge(oldCodiProdOfDetalleventaCollectionDetalleventa);
                }
            }
            for (Detalle detalleCollectionDetalle : producto.getDetalleCollection()) {
                Producto oldCodiProdOfDetalleCollectionDetalle = detalleCollectionDetalle.getCodiProd();
                detalleCollectionDetalle.setCodiProd(producto);
                detalleCollectionDetalle = em.merge(detalleCollectionDetalle);
                if (oldCodiProdOfDetalleCollectionDetalle != null) {
                    oldCodiProdOfDetalleCollectionDetalle.getDetalleCollection().remove(detalleCollectionDetalle);
                    oldCodiProdOfDetalleCollectionDetalle = em.merge(oldCodiProdOfDetalleCollectionDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getCodiProd()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getCodiProd());
            Collection<Detalleventa> detalleventaCollectionOld = persistentProducto.getDetalleventaCollection();
            Collection<Detalleventa> detalleventaCollectionNew = producto.getDetalleventaCollection();
            Collection<Detalle> detalleCollectionOld = persistentProducto.getDetalleCollection();
            Collection<Detalle> detalleCollectionNew = producto.getDetalleCollection();
            List<String> illegalOrphanMessages = null;
            for (Detalle detalleCollectionOldDetalle : detalleCollectionOld) {
                if (!detalleCollectionNew.contains(detalleCollectionOldDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detalle " + detalleCollectionOldDetalle + " since its codiProd field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Detalleventa> attachedDetalleventaCollectionNew = new ArrayList<Detalleventa>();
            for (Detalleventa detalleventaCollectionNewDetalleventaToAttach : detalleventaCollectionNew) {
                detalleventaCollectionNewDetalleventaToAttach = em.getReference(detalleventaCollectionNewDetalleventaToAttach.getClass(), detalleventaCollectionNewDetalleventaToAttach.getIddetalle());
                attachedDetalleventaCollectionNew.add(detalleventaCollectionNewDetalleventaToAttach);
            }
            detalleventaCollectionNew = attachedDetalleventaCollectionNew;
            producto.setDetalleventaCollection(detalleventaCollectionNew);
            Collection<Detalle> attachedDetalleCollectionNew = new ArrayList<Detalle>();
            for (Detalle detalleCollectionNewDetalleToAttach : detalleCollectionNew) {
                detalleCollectionNewDetalleToAttach = em.getReference(detalleCollectionNewDetalleToAttach.getClass(), detalleCollectionNewDetalleToAttach.getCodiDeta());
                attachedDetalleCollectionNew.add(detalleCollectionNewDetalleToAttach);
            }
            detalleCollectionNew = attachedDetalleCollectionNew;
            producto.setDetalleCollection(detalleCollectionNew);
            producto = em.merge(producto);
            for (Detalleventa detalleventaCollectionOldDetalleventa : detalleventaCollectionOld) {
                if (!detalleventaCollectionNew.contains(detalleventaCollectionOldDetalleventa)) {
                    detalleventaCollectionOldDetalleventa.setCodiProd(null);
                    detalleventaCollectionOldDetalleventa = em.merge(detalleventaCollectionOldDetalleventa);
                }
            }
            for (Detalleventa detalleventaCollectionNewDetalleventa : detalleventaCollectionNew) {
                if (!detalleventaCollectionOld.contains(detalleventaCollectionNewDetalleventa)) {
                    Producto oldCodiProdOfDetalleventaCollectionNewDetalleventa = detalleventaCollectionNewDetalleventa.getCodiProd();
                    detalleventaCollectionNewDetalleventa.setCodiProd(producto);
                    detalleventaCollectionNewDetalleventa = em.merge(detalleventaCollectionNewDetalleventa);
                    if (oldCodiProdOfDetalleventaCollectionNewDetalleventa != null && !oldCodiProdOfDetalleventaCollectionNewDetalleventa.equals(producto)) {
                        oldCodiProdOfDetalleventaCollectionNewDetalleventa.getDetalleventaCollection().remove(detalleventaCollectionNewDetalleventa);
                        oldCodiProdOfDetalleventaCollectionNewDetalleventa = em.merge(oldCodiProdOfDetalleventaCollectionNewDetalleventa);
                    }
                }
            }
            for (Detalle detalleCollectionNewDetalle : detalleCollectionNew) {
                if (!detalleCollectionOld.contains(detalleCollectionNewDetalle)) {
                    Producto oldCodiProdOfDetalleCollectionNewDetalle = detalleCollectionNewDetalle.getCodiProd();
                    detalleCollectionNewDetalle.setCodiProd(producto);
                    detalleCollectionNewDetalle = em.merge(detalleCollectionNewDetalle);
                    if (oldCodiProdOfDetalleCollectionNewDetalle != null && !oldCodiProdOfDetalleCollectionNewDetalle.equals(producto)) {
                        oldCodiProdOfDetalleCollectionNewDetalle.getDetalleCollection().remove(detalleCollectionNewDetalle);
                        oldCodiProdOfDetalleCollectionNewDetalle = em.merge(oldCodiProdOfDetalleCollectionNewDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getCodiProd();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodiProd();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Detalle> detalleCollectionOrphanCheck = producto.getDetalleCollection();
            for (Detalle detalleCollectionOrphanCheckDetalle : detalleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Detalle " + detalleCollectionOrphanCheckDetalle + " in its detalleCollection field has a non-nullable codiProd field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Detalleventa> detalleventaCollection = producto.getDetalleventaCollection();
            for (Detalleventa detalleventaCollectionDetalleventa : detalleventaCollection) {
                detalleventaCollectionDetalleventa.setCodiProd(null);
                detalleventaCollectionDetalleventa = em.merge(detalleventaCollectionDetalleventa);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

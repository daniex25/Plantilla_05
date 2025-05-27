/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Daniel Santamaria
 */
@Entity
@Table(name = "detalleventa")
@NamedQueries({
    @NamedQuery(name = "Detalleventa.findAll", query = "SELECT d FROM Detalleventa d"),
    @NamedQuery(name = "Detalleventa.findByIddetalle", query = "SELECT d FROM Detalleventa d WHERE d.iddetalle = :iddetalle"),
    @NamedQuery(name = "Detalleventa.findByCantidad", query = "SELECT d FROM Detalleventa d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detalleventa.findBySubtotal", query = "SELECT d FROM Detalleventa d WHERE d.subtotal = :subtotal")})
public class Detalleventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDDETALLE")
    private Integer iddetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private double cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUBTOTAL")
    private BigDecimal subtotal;
    @JoinColumn(name = "codiProd", referencedColumnName = "codiProd")
    @ManyToOne
    private Producto codiProd;
    @JoinColumn(name = "codiVent", referencedColumnName = "codiVent")
    @ManyToOne
    private Venta codiVent;

    public Detalleventa() {
    }

    public Detalleventa(Integer iddetalle) {
        this.iddetalle = iddetalle;
    }

    public Detalleventa(Integer iddetalle, double cantidad, BigDecimal subtotal) {
        this.iddetalle = iddetalle;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Integer getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(Integer iddetalle) {
        this.iddetalle = iddetalle;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getCodiProd() {
        return codiProd;
    }

    public void setCodiProd(Producto codiProd) {
        this.codiProd = codiProd;
    }

    public Venta getCodiVent() {
        return codiVent;
    }

    public void setCodiVent(Venta codiVent) {
        this.codiVent = codiVent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetalle != null ? iddetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleventa)) {
            return false;
        }
        Detalleventa other = (Detalleventa) object;
        if ((this.iddetalle == null && other.iddetalle != null) || (this.iddetalle != null && !this.iddetalle.equals(other.iddetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Detalleventa[ iddetalle=" + iddetalle + " ]";
    }
    
}

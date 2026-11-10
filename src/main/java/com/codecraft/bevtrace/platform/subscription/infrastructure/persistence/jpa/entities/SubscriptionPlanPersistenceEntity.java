package com.codecraft.bevtrace.platform.subscription.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Stored form of {@link SubscriptionPlan}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlanPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String code;
    private String name;
    private String descriptionKey;
    private double priceAmount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private BillingPeriod billingPeriod;
    private int maxRoutes;
    private int maxDevices;
    private int maxUsers;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "plan_features", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "feature_key")
    private List<String> featureKeys = new ArrayList<>();
    private boolean highlighted;
    private boolean custom;
    private boolean active;

    public static SubscriptionPlanPersistenceEntity fromDomain(SubscriptionPlan d) {
        var e = new SubscriptionPlanPersistenceEntity();
        e.setId(d.getId());
        e.code = d.getCode();
        e.name = d.getName();
        e.descriptionKey = d.getDescriptionKey();
        e.priceAmount = d.getPriceAmount();
        e.currency = d.getCurrency();
        e.billingPeriod = d.getBillingPeriod();
        e.maxRoutes = d.getMaxRoutes();
        e.maxDevices = d.getMaxDevices();
        e.maxUsers = d.getMaxUsers();
        e.featureKeys = d.getFeatureKeys();
        e.highlighted = d.getHighlighted();
        e.custom = d.isCustom();
        e.active = d.getActive();
        return e;
    }

    public SubscriptionPlan toDomain() {
        return SubscriptionPlan.restore(getId(), code, name, descriptionKey, priceAmount, currency, billingPeriod, maxRoutes, maxDevices, maxUsers, featureKeys, highlighted, custom, active);
    }
}

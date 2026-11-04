package com.codecraft.bevtrace.platform.subscription.domain.model.aggregates;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.util.ArrayList;
import java.util.List;

/**
 * A commercial plan (public catalogue). Custom plans (enterprise) are quoted, not purchased online.
 */
public class SubscriptionPlan extends AbstractDomainAggregateRoot<SubscriptionPlan> {

    private Long id;
    private String code;
    private String name;
    private String descriptionKey;
    private double priceAmount;
    private String currency;
    private BillingPeriod billingPeriod;
    private int maxRoutes;
    private int maxDevices;
    private int maxUsers;
    private List<String> featureKeys = new ArrayList<>();
    private boolean highlighted;
    private boolean custom;
    private boolean active;

    protected SubscriptionPlan() {
    }

    public String getCode() {
        return code;
    }

    public double getPriceAmount() {
        return priceAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public boolean isCustom() {
        return custom;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public int getMaxRoutes() {
        return maxRoutes;
    }

    public int getMaxDevices() {
        return maxDevices;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public List<String> getFeatureKeys() {
        return featureKeys;
    }

    public boolean getHighlighted() {
        return highlighted;
    }

    public boolean getActive() {
        return active;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static SubscriptionPlan restore(Long id, String code, String name, String descriptionKey, double priceAmount, String currency, BillingPeriod billingPeriod, int maxRoutes, int maxDevices, int maxUsers, List<String> featureKeys, boolean highlighted, boolean custom, boolean active) {
        var a = new SubscriptionPlan();
        a.id = id;
        a.code = code;
        a.name = name;
        a.descriptionKey = descriptionKey;
        a.priceAmount = priceAmount;
        a.currency = currency;
        a.billingPeriod = billingPeriod;
        a.maxRoutes = maxRoutes;
        a.maxDevices = maxDevices;
        a.maxUsers = maxUsers;
        a.featureKeys = featureKeys;
        a.highlighted = highlighted;
        a.custom = custom;
        a.active = active;
        return a;
    }
}

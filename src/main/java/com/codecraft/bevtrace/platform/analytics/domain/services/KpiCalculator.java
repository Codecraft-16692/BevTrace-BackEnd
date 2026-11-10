package com.codecraft.bevtrace.platform.analytics.domain.services;

import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.Indicators;
import com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects.OperationalData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Pure domain service that computes the logistics indicators of a period (days are UTC, bounds inclusive).
 */
public final class KpiCalculator {

    private KpiCalculator() {
    }

    /**
     * @param data  operational facts
     * @param start first day of the period
     * @param end   last day of the period
     * @return the indicators; all zero (and {@code hasData=false}) for an empty period
     */
    public static Indicators calculate(OperationalData data, LocalDate start, LocalDate end) {
        List<OperationalData.Order> delivered = data.orders().stream()
                .filter(o -> o.delivered() && within(o.deliveredAt() == null ? null : day(o.deliveredAt()), start, end))
                .toList();
        var wasteInPeriod = data.waste().stream().filter(w -> within(w.reportedDate(), start, end)).toList();
        var reconciled = data.reconciliations().stream()
                .filter(r -> r.reconciled() && within(r.date(), start, end)).toList();

        long onTimeInFull = delivered.stream()
                .filter(o -> o.deliveredQuantity() != null && o.deliveredQuantity() >= o.requestedQuantity())
                .filter(o -> !day(o.deliveredAt()).isAfter(o.scheduledDate()))
                .count();
        long requested = delivered.stream().mapToLong(OperationalData.Order::requestedQuantity).sum();
        long shipped = delivered.stream().mapToLong(o -> o.deliveredQuantity() == null ? 0 : o.deliveredQuantity()).sum();
        long stock = data.batches().stream().filter(OperationalData.Batch::available)
                .mapToLong(OperationalData.Batch::currentQty).sum();
        long received = data.batches().stream()
                .filter(b -> b.receivedAt() != null && !day(b.receivedAt()).isAfter(end))
                .mapToLong(OperationalData.Batch::initialQty).sum();
        long lost = wasteInPeriod.stream().mapToLong(OperationalData.Waste::quantity).sum();

        return new Indicators(
                delivered.isEmpty() ? 0 : round((double) onTimeInFull / delivered.size() * 100),
                requested == 0 ? 0 : round((double) shipped / requested * 100),
                reconciled.isEmpty() ? 0 : round(reconciled.stream()
                        .mapToDouble(OperationalData.Reconciliation::eriPercentage).average().orElse(0)),
                stock == 0 ? 0 : round((double) shipped / stock),
                received == 0 ? 0 : round((double) lost / received * 100),
                !delivered.isEmpty() || !wasteInPeriod.isEmpty() || !reconciled.isEmpty());
    }

    private static LocalDate day(Instant instant) {
        return instant.atZone(ZoneOffset.UTC).toLocalDate();
    }

    private static boolean within(LocalDate day, LocalDate start, LocalDate end) {
        return day != null && !day.isBefore(start) && !day.isAfter(end);
    }

    private static double round(double value) {
        return Math.round(value * 100) / 100.0;
    }
}

package org.h2.expression.aggregate;

import org.h2.engine.SessionLocal;
import org.h2.value.Value;
import org.h2.value.ValueNull;

/**
 * Data stored while calculating SECOND_MAX.
 */
final class AggregateDataSecondMax extends AggregateData {

    private Value largest;
    private Value secondLargest;

    @Override
    void add(SessionLocal session, Value v) {
        if (v == ValueNull.INSTANCE) {
            return;
        }

        if (largest == null) {
            largest = v;
            return;
        }

        int comparison = session.compare(v, largest);
        if (comparison > 0) {
            secondLargest = largest;
            largest = v;
        } else if (comparison < 0
                && (secondLargest == null || session.compare(v, secondLargest) > 0)) {
            secondLargest = v;
        }
    }

    @Override
    Value getValue(SessionLocal session) {
        return secondLargest == null ? ValueNull.INSTANCE : secondLargest;
    }
}
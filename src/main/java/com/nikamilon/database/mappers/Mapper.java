package com.nikamilon.database.mappers;

import java.util.Optional;

public interface Mapper<A,B> {
    B mapTo(A a);

    A mapFrom(B b);
}

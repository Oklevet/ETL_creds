package ru.etl.extract;

import ru.etl.model.PrCred;

import java.util.Collection;

public interface ExtractorCFT {
    Collection<PrCred> process(Object key, String sql);
}

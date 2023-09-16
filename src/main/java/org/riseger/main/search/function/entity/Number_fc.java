package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.NumberFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.NUMBER;

public class Number_fc extends NumberFunction_c {
    Number n;

    public Number_fc(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        NUMBER nbmf = (NUMBER)condition;
        this.n = nbmf.getNumber();
    }

    @Override
    public Number resolve(Element_c element) {
        super.set(n);
        return n;
    }
}

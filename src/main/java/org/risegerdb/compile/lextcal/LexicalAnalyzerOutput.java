package org.risegerdb.compile.lextcal;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class LexicalAnalyzerOutput {
    private List<String> lexemeList = new LinkedList<>();

    private Map<String,String> stringConstTable = new HashMap<>();

    private Map<String,Double> numberConstTable = new HashMap<>();

    public void add(Lexicon lexicon) {
        if(lexicon.getType().equals(Lexicon.Type.FUN)) {
            lexemeList.add(lexicon.getId());
        } else if(lexicon.getType().equals(Lexicon.Type.NUM)) {
            if(!numberConstTable.containsKey(lexicon.getId())) {
                numberConstTable.put(lexicon.getId(), lexicon.getDoubleValue());
            }
            lexemeList.add(lexicon.getId());
        } else if(lexicon.getType().equals(Lexicon.Type.STR)) {
            if(!stringConstTable.containsKey(lexicon.getId())) {
                stringConstTable.put(lexicon.getId(), lexicon.getValue());
            }
            lexemeList.add(lexicon.getId());
        }
    }
}

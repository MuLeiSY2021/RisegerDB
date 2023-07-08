package org.risegerdb.compile.syntax;

import lombok.Data;
import org.risegerdb.compile.lextcal.Lexicon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class InterpreterOutput {
    private List<String> lexemeList = new LinkedList<>();

    private Map<String,String> stringConstTable = new HashMap<>();

    private Map<String,Double> numberConstTable = new HashMap<>();

    public void add(Lexicon lexicon) {
        if(lexicon.getType().equals(Lexicon.Type.FUN)) {
            lexemeList.add(lexicon.getValue());
        } else if(lexicon.getType().equals(Lexicon.Type.NUM)) {
            if(!numberConstTable.containsKey(lexicon.getId())) {
                numberConstTable.put(lexicon.getId(), lexicon.getDoubleValue());
            }
            lexemeList.add(lexicon.getValue());
        } else if(lexicon.getType().equals(Lexicon.Type.STR)) {
            if(!stringConstTable.containsKey(lexicon.getId())) {
                stringConstTable.put(lexicon.getId(), lexicon.getValue());
            }
            lexemeList.add(lexicon.getValue());
        }
    }
}

package org.dscvit.in;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        String inFile = args[0];
        String outFile = args[1];
        System.out.println(inFile);
        System.out.println(outFile);
        new File(outFile).createNewFile();
        String out = prototypeConverter(Files.readString(Paths.get(inFile)));
        Files.writeString(Paths.get(outFile), out);
        System.out.println(out);
    }

    static String PATTERN_NO_TYPEDEF = "struct (\\w+)\\s*?\\{[\\w\\W]*?(([\\w]*)? ([\\w]*)\\(\\) \\{([\\w\\W]*?)\\})[\\w\\W]*?\\};";

    static String PATTERN_NO_TYPDEF_ONLY = "struct (\\w+)\\s*?\\{([\\w\\W]*)\\};";
    static String PATTERN_FUNCTIONS_ONLY = "([\\w]*)? ([\\w]*)\\(\\) \\{([\\w\\W]*?)\\}";

//    static String PATTERN_LOOKAHEAD = "(?=(" + PATTERN_NO_TYPEDEF + ")).";
    private static String prototypeConverter(String input) {
        Pattern p = Pattern.compile(PATTERN_NO_TYPDEF_ONLY);
        Pattern p_inner = Pattern.compile(PATTERN_FUNCTIONS_ONLY);
        Matcher m = p.matcher(input);
        return m.replaceAll(matchResult -> {
            String structname = matchResult.group(1);
            Matcher m2 = p_inner.matcher(matchResult.group(2));
            Matcher m3 = p_inner.matcher(matchResult.group(2));
//            System.out.println("1: " + matchResult.group(1));
//            System.out.println("2: " + matchResult.group(2));

//            System.out.println("3: " + matchResult.group(3));
//            System.out.println("4: " + matchResult.group(4));
//            System.out.println("5: " + matchResult.group(5));
//            System.out.println("FES: " + fes);
            Stream<String> funcs = m2.results().map(t -> {
                String func = t.group(1) + " " + t.group(2) + "(struct " + matchResult.group(1) + " *this) {";
                func += t.group(3);
                func += "}\n";
                return func;
            });
            String leftout = m3.replaceAll(mr -> "");
            String fes = input.substring(matchResult.start(), matchResult.start(2)) + leftout + input.substring(matchResult.end(2), matchResult.end());
            String finalfinal = funcs.reduce(fes, (res, ele) -> res + "\n" + ele);
//            String func = matchResult.group(3).trim() + " " + matchResult.group(4) + "(struct " + matchResult.group(1) + " *this) {";
//            func += matchResult.group(5);
//            func += "}\n";
//            return fes + "\n" + func;
            return finalfinal;
        });
    }


}

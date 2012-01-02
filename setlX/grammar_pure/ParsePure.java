import org.antlr.runtime.*;

public class ParsePure {

    public static void main(String[] args) throws Exception {
        ANTLRInputStream    input   = new ANTLRInputStream(System.in);
        PureLexer           lexer   = new PureLexer(input);
        CommonTokenStream   ts      = new CommonTokenStream(lexer);
        PureParser          parser  = new PureParser(ts);

        parser.initBlock();

        // fill token stream until EOF is reached (parser fills only as far as its lookahead needs)
        ts.fill();
        // current index in stream of tokens
        int index = ts.index();
        
        /*
         *  If the index into the tokenStream (which was set by the parser)
         *  is not equal to the number of tokens in the complete input (minus EOF),
         *  some tokens where ignored by the parser.
         *
         *  Best guess: The parser encountered a syntax error after a valid rule.
         */
        if (index < (ts.size() - 1)) {
            // parse again to force displaying syntax error in remaining tokenStream
            parser.initBlock();
            
            // check if parser moved the index
            if (index == ts.index()) {
                /*  Index was not moved. Probably epsilon can be derived from the start-rule.
                 *  However there are still tokens left...
                 */
                System.err.println("ParsePure: Input includes unidentified errors!");
            }
        }

        int errors = parser.getNumberOfSyntaxErrors();
        if (errors > 0) {
            System.err.println("ParsePure: " + errors + " syntax errors encountered in the input!");
        }
    }
}


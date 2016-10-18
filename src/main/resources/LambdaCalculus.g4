grammar LambdaCalculus;

program:        expression
            |
            ;

expression:     Identifier                                                  # variable
            |   (Int | Bool)                                                # constant
            |   expression expression                                       # application
            |   '(' Identifier ')' '=>' expression                          # abstraction
            |   'let' Identifier '=' expression 'in' body                   # letIn
            |   'if' (expression) 'then' body 'else' body                   # ifThenElse
            |   expression Operator expression                              # binaryExpression
            |   '(' expression ')'                                          # brackets
            ;

body: expression;

Identifier: ('a'..'z') ('a'..'z' | '0'..'9')*;
Operator: '+' | '-' | '*' | '/' | '<' | '>' | '<=' | '>=' | '==';
Int:    [0-9]+;
Bool:   'TRUE' | 'FALSE';
WS:     [ \n\t\r]+ -> skip;
// http://pubs.opengroup.org/onlinepubs/007908799/xbd/re.html
grammar BasicRegex;

r : 'abcdefgh';

basic_reg_exp : L_ANCHOR? re_expression R_ANCHOR?;

re_expression : simple_re+;
simple_re : nondupl_re re_dupl_symbol?;
nondupl_re:  one_character_re
               | BACK_OPEN_PAREN re_expression BACK_CLOSE_PAREN
               | BACK_OPEN_PAREN BACK_CLOSE_PAREN
               | BACKREF;
re_dupl_symbol: '*'
               | BACK_OPEN_BRACE DUP_COUNT ','? BACK_CLOSE_BRACE
               | BACK_OPEN_BRACE DUP_COUNT ',' DUP_COUNT BACK_CLOSE_BRACE
               ;
one_character_re: ORD_CHAR;
     //          | QUOTED_CHAR
       //        | '.'
         //      | bracket_expression;

// Bracket expression
BRACKET_EXPRESSION_OPEN : '['  -> pushMode(BRACKET_EXPRESSION_MODE);

bracket_expression : BRACKET_EXPRESSION_OPEN matching_list BRACKET_EXPRESSION_CLOSE |
        BRACKET_EXPRESSION_OPEN nonmatching_list BRACKET_EXPRESSION_CLOSE ;

//mode BRACKET_EXPRESSION_MODE;
BRACKET_EXPRESSION_CLOSE : ']' -> popMode(BRACKET_EXPRESSION_MODE);
               
matching_list  : bracket_list
               ;
nonmatching_list : '^' bracket_list
               ;
bracket_list   : follow_list
               | follow_list '-'
               ;
follow_list    :             expression_term
               | follow_list expression_term
               ;
expression_term : single_expression
               | range_expression
               ;
single_expression : end_range
               | character_class
               | equivalence_class
               ;
range_expression : start_range end_range
               | start_range '-'
               ;
start_range    : end_range '-'
               ;
end_range      : COLL_ELEM
               | collating_symbol
               ;
collating_symbol : OPEN_DOT COLL_ELEM DOT_CLOSE
               | OPEN_DOT META_CHAR DOT_CLOSE
               ;
equivalence_class : OPEN_EQUAL COLL_ELEM EQUAL_CLOSE
               ;
character_class : OPEN_COLON CLASS_NAME COLON_CLOSE
               ;

// tokens
CLASS_NAME: [a-zA-Z]+;

COLL_ELEM: ~[^~]; // all but metachars
BACK_OPEN_PAREN: '(';
BACK_CLOSE_PAREN: ')';
BACK_OPEN_BRACE: '{';
BACK_CLOSE_BRACE: '}';
L_ANCHOR: '^';
R_ANCHOR: '$';
BACKREF: '\\' INTEGER;
DUP_COUNT: INTEGER+;
ORD_CHAR : 'a';
TODO : 'todo';
QUOTED_CHAR: '\\' [^.*[$\\];
OPEN_DOT: '[.';
DOT_CLOSE: '.]';
OPEN_EQUAL: '[=';
EQUAL_CLOSE: '=]';
OPEN_COLON: '[:';
COLON_CLOSE: ':]';
fragment INTEGER: [0-9];		
META_CHAR: [^-]];

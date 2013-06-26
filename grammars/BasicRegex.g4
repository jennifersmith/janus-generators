// http://pubs.opengroup.org/onlinepubs/007908799/xbd/re.html
grammar BasicRegex;

r : basic_reg_exp;

basic_reg_exp : L_ANCHOR? re_expression R_ANCHOR?;

re_expression : simple_re+;
simple_re : nondupl_re re_dupl_symbol?;
nondupl_re:  one_character_re
               | BACK_OPEN_PAREN re_expression BACK_CLOSE_PAREN
               | BACK_OPEN_PAREN BACK_CLOSE_PAREN
               | BACKREF;
re_dupl_symbol: TODO;
one_character_re: TODO;

// tokens
BACK_OPEN_PAREN: '(';
BACK_CLOSE_PAREN: ')';
L_ANCHOR: '^';
R_ANCHOR: '$';
BACKREF: '\\' INTEGER;

TODO : 'todo';

fragment INTEGER: [0-9];		
# Step 1 : 24th June ->

Figuring out how/if I can use ANTLR to write a basic regex parsing grammar. Maybe something more useful exists too.

## Refs

http://stackoverflow.com/questions/14667781/antlr-4-and-ast-visitors

## Progress 26th June 09:33 !

Well after a lot of effort (hours not days thankfully) I managed to create a basic hello world grammar using ANLTR, used it to parse a basic 'hello foo' string and spit the result out as a map that is converted from the parse context structure that comes out of the parser:

```clojure
(parse-stuffs "hello lamppost") => {:rule-index 0, :children ("hello" "lamppost")}
```

The rule-index refers to the list of parse rules inside the custom HelloParser - so I don't exactly know which one it is.

That's a nice little demonstration of using ANTLR from Clojure. I didn't actually think to google that particular phrase, meaning I missed [this](https://github.com/briancarper/clojure-antlr-example/blob/master/src/antlr_example/core.clj). At least I grok ANTLR a little more than I did before. And I have a multimethod for converting the tree to a map which is always fun.

It's a nice little demonstration, but it doesn't actually get me any further. I need to be able to take an arbitrary grammar and try and use that to generate all possible adherents to that. So take the basic hello world example:

```
// Define a grammar called Hello
grammar Hello;
r  : 'hello' ID ;         // match keyword hello followed by an identifier
ID : [a-z]+ ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
```

I want to generate ("hello a" "hello b" "hello c") etc. This isn't quite the same as generating stuff based on regular expressions but it's a good start.

I was talking to Phil about what I am trying to do and he took me through a chunk of language theory in a couple of minutes ! I learned about [Chomsky's Hierarchy](http://en.wikipedia.org/wiki/Chomsky_hierarchy) of classes of grammars and some of the problems of perl-style regexes that depart from the rules of regular grammars. I have to avoid the thoughts of [monkeys playing cymbals](https://www.youtube.com/watch?v=_73NU6OlNuw) that go through my head at the start of any mention of theoretical computer science stuff if I want to make this happen!
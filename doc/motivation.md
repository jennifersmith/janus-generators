# Motivation and background

## Two Awesome Things

### Generative testing

Generative testing (as embodied by the Clojure library [test.generative](https://github.com/clojure/test.generative) ) is a form of testing where instead of describing and testing behaviour with specific inputs and outputs in your tests, you give the test framework an idea of the kind of input it should accept and overall rules to which the output should conform.

For example, if you were testing your math library is able to deal with integer addition correctly, you could write tests like this:

* _Given_ two numbers 10 and 15
* _When_ I add these together
* _Then_ The answer should be 25

That is good for testing the basic behaviour (1+2=3) But you are interested in testing some general addition [properties](http://en.wikipedia.org/wiki/Addition#Properties). You could write a bunch more examples:

Identity:
* _Given_ one number 115, 556, 11, 55, 112 etc.
* _When_ I add this to 0
* _Then_ the answer should be 115, 556, 11, 55, 112 etc.

Basic associativity:

* _Given_ three numbers [1,2,3] or [50,10,100]
* _When_ I add (a + b) + c or a + (b + c) or a + b + c
* _Then_ all answers are the same

... boring!

Well, not boring but at the point at which you feel confident in the test coverage of a particular rule, you have written a large set of examples that you have to hold together with documentation to make sure it's clear what rule they are proving.

Instead imagine you could write the test by stating the rule itself. For example:

Identity:

* _Given_ any integer
* _When_ I add it to zero
* _Then_ the result is the same as the original

Associativity

* _Given_ N integers split into M random parenthesized groups
* _When_ I add these together in different permutations of groups (a + b) + c , a + (b + c) etc.
* _Then_ The result is the same 

This is what generative testing gives you: the ability to define an input specification (e.g. integers, or sets of integers), a means of computing the output and a means of checking this input based on the original inputs. The tests will be run, generating further and further inputs until you either stop running them (by a minimum number of examples or a running time) or the inputs are exhausted. Rather than writing tests until you are confident in your test coverage, you can _run_ tests until you are confident in your test coverage.

As you can see from the examples, the sweet spot for generative testing is in invariant testing - that is, testing properties that should always for a particular set of inputs.

Generative testing is enabled in Clojure by [test.generative](https://github.com/clojure/test.generative) and you can see it in action testing - suprisingly - the integer properties in the [number tests](https://github.com/clojure/clojure/blob/master/test/clojure/test_clojure/numbers.clj). Aaron Bedra's [Generative Generation](http://www.infoq.com/presentations/Clojure-Generative-Testing) talk is also a great introduction to the library and generative testing concept.x

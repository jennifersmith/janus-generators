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

Generative testing is enabled in Clojure by [test.generative](https://github.com/clojure/test.generative) and you can see it in action testing - suprisingly - the integer properties in the [number tests](https://github.com/clojure/clojure/blob/master/test/clojure/test_clojure/numbers.clj). Aaron Bedra's [Generative Generation](http://www.infoq.com/presentations/Clojure-Generative-Testing) talk is also a great introduction to the library and generative testing concept.


### Janus

[Janus](https://github.com/gga/janus) is a library developed by my colleague [Giles Alexander](http://overwatering.org/) that is intended use a clojure-expressed web service contract to create the following:

* A suite of tests that test that the service conforms to the contract
* A service stub/mock that can be used in place of the service for the consumer to run 'offline'

The latter part is not actually implemented just yet.

![Janus in Action](https://dl.dropboxusercontent.com/u/18288740/janus-generators/janus.jpg)

The idea is that by expressing your (hopefully consumer-driven) contract in a format recognised by janus, you would be able to verify upstream services and run independently of these as required. If you have many consumers of the same service, this would greatly simplify the effort in figuring out who needs what. 

The contracts have to have some notion of the content of the data that the service expects to recieve and that the service itself resturns back. In the current implementation, this supports [JsonPath](https://code.google.com/p/json-path/) to specify parts of the responses and requests that should be present.

## The big idea

So we have generative testing that allows us to test general rules and behaviour of our code by specifying input generation rules and general verification. We also have Janus which allows us to specify data formats and contracts that services can/should adhere to for both testing services and providing support for consumers.

The idea of this project is to start creating generators based on verification formats. These can be used to both generate data for testing and stubbing services for the purposes of Janus and as generators for test.generative to allow us to do generative testing on a wider range of data formats than what are currently available.

### Possible formats (in order of likelihood)

+ JsonPath
+ Regex
+ Basic XPath
+ Schematron

It might be fun also to see if anything in this approach can be generified so we can give an output specification for any form of regular data:

+ [edn](https://github.com/edn-format/edn)
+ [CIF for network rail train data](http://www.atoc.org/clientfiles/files/RSPDocuments/20070801.pdf)
+ [leipzig](https://github.com/ctford/leipzig) melody format (or perhaps "Valid Bach Chorale" as a subset)

### Sample usage

I would expect something like this to work:

```clojure

(gen/passing-regex #"A+") => ["A", "AA", "AAA" ...
 (gen/passing-jsonpath "$.foo.bar" (gen/passing-regex #"\d")) => ["{foo:{bar:1}}", "{foo:{bar:2}}" ...

```

And maybe eventually:

```clojure

(defgenerator cif-generator "cif-input-spec")

(cif-generator) => ["LICMDNJN 1915 00000000 FL", "LICMDNJN 1915 00000000 BA " ... 

```

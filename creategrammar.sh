lein clean
rm -rf src/java/grammars/
lein run -m org.antlr.v4.Tool grammars/*.g4  -o src/java/ # -package janus_generators.grammars



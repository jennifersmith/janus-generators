(defproject janus-generators "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.antlr/antlr4-runtime "4.0"]
                 [org.antlr/antlr4-maven-plugin "4.0"]
                 [instaparse "1.1.0"]
                 [org.clojure/core.logic "0.8.3"]
                  [midje "1.5.1" :scope "test"]]
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"])
